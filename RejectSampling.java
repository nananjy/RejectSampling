package 包名;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import RejectSampling.BaseEdge;
import RejectSampling.BaseVertex;
import RejectSampling.BasicDirectedGraph;
import Utils.FilePathUtils;

/**
 * 拒绝采样算法实现
 * @author 作者名
 */
public class RejectSampling {

	private static int SEGMENT_Times;//迭代次数
	private static float Epsilon;//常量segmer，判停条件
	private static int MAX_Times;//最大迭代运行次数
	private float preProbability;//上一次的频率
	private float nowProbability;//当前的频率
	private BaseVertex goalVertex;//goalVertext存放待查询节点
	private int goalStatus;//goalStatus存放待查询节点的状态
	private Map<String, Integer> evidenceMap;//evidenceMap存放证据节点集
	private ArrayList<String> vertexList;//vertexList存放由父节点到子节点的节点列表，根据参数表生成随机样本时，按vertexList顺序生成 
	private BasicDirectedGraph bayesianNetwork;//有向图
	private int totalTimesCounter;//计算采样次数的变量
	
	/**
	 * 构造函数，用于生成有向图，节点列表及为变量赋值
	 * @param nodeFileName 存放节点的文件名
	 * @param edgeFileName 存放边的文件名
	 * @param questionFileName 存放待查询节点及证据节点的文件名
	 * @param segment_times 迭代次数
	 * @param epsilon 待查询节点前后两次发生频率之差的阈值，判停条件
	 * @param max_times 最大迭代次数
	 */
	public RejectSampling(String nodeFileName, String edgeFileName, String questionFileName, int segment_times, float epsilon, int max_times) {
		//生成贝叶斯网
		this.bayesianNetwork = new BasicDirectedGraph(nodeFileName, edgeFileName);
		//判断是否生成贝叶斯网
		if(this.bayesianNetwork == null)
		{
			System.out.println("Can Not Construct Network!");
			return ;
		}
		//生成从父节点开始的节点列表
		generateVertexList();
		//读取questionFile，获取待查询节点、待查询的状态及证据节点的状态
		String questingFilePath = FilePathUtils.GenFilePath(questionFileName);
		try{
			BufferedReader br = new BufferedReader(new FileReader(questingFilePath));
			String tempStr = br.readLine();
			String[] goalArcs = tempStr.split(" ");
			if(goalArcs.length != 2){
				System.out.println("Can't Find Goal In File!");
				br.close();
				return ;
			}
			this.goalVertex = this.bayesianNetwork.getVertexWithLabel(goalArcs[0]);//goalVertext存放待查询节点
			this.goalStatus = Integer.parseInt(goalArcs[1]);//goalStatus存放待查询节点的状态
			this.evidenceMap = new HashMap<String, Integer>();//获取证据节点集
			tempStr = br.readLine();
			while(tempStr != null){
				String[] arcs = tempStr.split(" ");
				evidenceMap.put(arcs[0], Integer.parseInt(arcs[1]));
				tempStr = br.readLine();
			}
			br.close();
		}catch(Exception e){
			throw new RuntimeException();
		}
		RejectSampling.SEGMENT_Times = segment_times;//赋值迭代次数
		RejectSampling.Epsilon = epsilon;//常量segmer，判停条件
		RejectSampling.MAX_Times = max_times;//赋值最大迭代次数
	}
	
	/*
	 * 多次循环，当preProbability和nextProbability近乎相等，或者迭代次数超过最大迭代次数时，循环结束
	 */
	public void getGoalVertexProbability() {
		this.preProbability = -1;//为上一次待查询节点发生的频率赋初值
		this.nowProbability = -2;//为当前待查询节点发生的频率赋初值
		this.totalTimesCounter = 0;//为总循环次数赋初值
		while (Math.abs(nowProbability - preProbability) > RejectSampling.Epsilon && totalTimesCounter < RejectSampling.MAX_Times) {
			int gsEmergeCounter = 0;//目标节点状态出现的次数
			//循环RejectSampling.SEGMENT_Times次，生成采样样本
			for (int i = 0; i < RejectSampling.SEGMENT_Times; i++) {
				//根据贝叶斯网的参数表生成随机样本，即根据vertexList列表的顺序生成
				getGoalVertexStatus();//生成满足证据节点状态的随机样本
				int gStatus = this.goalVertex.getStatus();//获取带查询节点的状态
				//判断生成的样本中证据节点的状态与目标状态goalStatus是否匹配
				if (gStatus == this.goalStatus) {
					//如果匹配，将待查询节点的取值个数加一
					gsEmergeCounter++;
				}
			}
			this.preProbability = this.nowProbability;//将待查询节点当前发生的频率赋给上一次发生的频率
			this.nowProbability = (float) gsEmergeCounter/RejectSampling.SEGMENT_Times;//计算待查询节点当前发生的频率
			System.out.println("待查询节点当前发生的频率：" + this.nowProbability);
			System.out.println("当前执行次数： " + totalTimesCounter + "；目标节点" + this.goalVertex.getLabel() + "发生的频率： " + gsEmergeCounter + "/" + RejectSampling.SEGMENT_Times);
		}
		if((Math.abs(this.nowProbability - this.preProbability) <= RejectSampling.Epsilon)) {//preProbability和nextProbability近乎相等
			System.out.println("Hit Stable_Probability END!");
		}else {//迭代次数超过最大迭代次数
			System.out.println("Hit Max_Times END!");
		}
		for (int i = 0; i < this.vertexList.size(); i++) {//输出节点状态
			System.out.println("节点状态：" + this.bayesianNetwork.getVertexWithLabel(this.vertexList.get(i)));
		}
		System.out.println("待查询节点状态发生的概率为：" + (this.nowProbability + this.preProbability)/2);
	}

	/**
	 * 通过节点名称label，生成随机样本
	 * @param label
	 */
	public void generateSample(String label) {
		BaseVertex baseVertex = this.bayesianNetwork.getVertexWithLabel(label);//通过节点名称label获取节点，假定节点为X
		float randomNum = (float)Math.random();//生成节点的随机值
		String statusStr = "1";//设置节点的状态
		//通过getHeadLink方法得到节点的第一条边，从而获取节点的父节点状态
		String parentStatus = "";//存放父节点的状态parent
		BaseEdge bEdge = baseVertex.getHeadLink();
		while (bEdge != null) {
			parentStatus += this.bayesianNetwork.getVertexWithID(bEdge.getTailVertexID()).getStatus();
			bEdge = bEdge.getHeadNext();
		}
		statusStr += parentStatus;
		//判断生成的随机值randomNum是否大于节点发生的概率P(X=1|parent),从而确定X的值
		if (randomNum >= baseVertex.getConditionalProbability(statusStr)) {//getConditionalProbability方法用于获取节点发生的概率
			baseVertex.setStatus(1);
		}else {
			baseVertex.setStatus(0);
		}
	}
	
	/*
	 * 获得待查询目标节点的状态
	 */
	public void getGoalVertexStatus() {
		totalTimesCounter++;
		for (String vertexLabel : vertexList) {
			generateSample(vertexLabel);//生成所有节点的状态
		}
		//测试代码，假定有4个节点，生成随机值的顺序如下：
		/*String[] labels = {"X4", "X2", "X3", "X1"};
		for (String label : labels) {
			generateSample(label);
		}*/
		//判断生成的随机样本中证据节点的状态与给定证据节点的状态是否匹配
		for (Map.Entry<String, Integer> entry : evidenceMap.entrySet()) {
			//获取生成的证据节点entry的状态
			int eStatus = this.bayesianNetwork.getVertexWithLabel(entry.getKey()).getStatus();
			if (eStatus != entry.getValue()) {
				//如果不匹配，重新生成随机样本
				getGoalVertexStatus();
				break;
			}
		}
	}
	
	/*
	 * 通过有向图生成从父节点开始的节点列表，用于生成随机样本
	 */
	public void generateVertexList() {
		//初始化节点列表
		this.vertexList = new ArrayList<String>();
		//获得bayesianNetwork的所有节点
		ArrayList<BaseVertex> bayesianVertexList = this.bayesianNetwork.getVertexList();
		//1.遍历所有节点，找出父节点个数为0的节点（贝叶斯网属于有向无环图）
		for (int i = 0; i < bayesianVertexList.size(); i++) {
			if (bayesianVertexList.get(i).getHeadLink() == null) {//getHeadLink方法用于获取节点的第一条入度边
				//将节点存入vertexList里，此时vertexList里的节点不会重复
				this.vertexList.add(bayesianVertexList.get(i).getLabel());
			}
		}
		int parVertexBegLoc = 0;//存放父节点在vertexList开始的位置
		while (parVertexBegLoc < this.vertexList.size()) {
			ArrayList<String> childVertexList = new ArrayList<String>();//暂存儿子节点，最后再存入vertexList里
			//2.从父节点出发，找儿子节点，将其存入childVertexList
			for (int i = parVertexBegLoc; i < this.vertexList.size(); i++) {
				BaseEdge edge = this.bayesianNetwork.getVertexWithLabel(this.vertexList.get(i)).getTailLink();
				while (edge != null) {
					childVertexList.add(this.bayesianNetwork.getVertexWithID(edge.getHeadVertexID()).getLabel());
					edge = edge.getTailNext();
				}
			}
			parVertexBegLoc = this.vertexList.size();//给parVertexBegLoc赋值
			//3.将childVertexList存入vertexList，去掉重复节点，并按节点最后一次插入的顺序排序
			for (String childVertex : childVertexList) {
				for (int i = 0; i < vertexList.size(); i++) {
					if (childVertex.equals(vertexList.get(i))) {
						vertexList.remove(i);
						parVertexBegLoc--;
						break;
					}
				}
				vertexList.add(childVertex);
			}
			//4.从parVertexBegLoc的位置开始继续寻找子节点
		}
		System.out.println("bayesianNetwork中节点的列表为：" + bayesianVertexList.toString());
		System.out.println("生成的从父节点到子节点的节点列表为：" + this.vertexList.toString());
	}

	public static void main(String[] args) {
		//nodes.txt存放节点及参数表，edges.txt存放边，questions.txt存放待查询节点及证据节点状态
		RejectSampling rejectSampling = new RejectSampling("nodes.txt", "edges.txt", "questions.txt", 1000, 0.001f, 100000);
		rejectSampling.getGoalVertexProbability();
	}
}
