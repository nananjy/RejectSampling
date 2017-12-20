package 包名;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import Utils.FilePathUtils;

/**
 * 十字链表的有向图类
 * @author 作者名
 */
public class BasicDirectedGraph {

	private int vertexId;//存放节点编号，按文件nodes.txt中存放次序编号0,1...
	private ArrayList<BaseVertex> vertexList;//存放所有节点的集合
	private Map<String, Integer> vertexMap;//存放节点名称对应的编号
	
	/**
	 * 输入节点文件、边文件的构造函数
	 * @param nodeFileName 节点文件名
	 * @param edgeFileName 边文件名
	 */
	public BasicDirectedGraph(String nodeFileName, String edgeFileName) {
		this.vertexId = 0;//初始化节点编号
		this.vertexList = new ArrayList<BaseVertex>();//初始化节点列表
		String nodefilepath = FilePathUtils.generateFilePath(nodeFileName);//获得节点文件路径
		String edgefilepath = FilePathUtils.generateFilePath(edgeFileName);//获得边文件路径
		this.vertexMap = new HashMap<String, Integer>();//初始化节点、编号映射
		try {
			BufferedReader nodebr = new BufferedReader(new FileReader(nodefilepath));
			String tempStr1 = nodebr.readLine();//读取节点文本nodes.txt的第一行内容，即节点名称
			String tempStr2, tempStr3;
			while(tempStr1 != null) {
				tempStr2 = nodebr.readLine();//读取节点文本nodes.txt的第二行内容，即节点及其父节点的取值个数，2表示节点有两种取值，有n个状态就有n-1个父节点
				tempStr3 = nodebr.readLine();//读取节点文本nodes.txt的第三行内容，即节点的参数表CPT，存放的方式为00、01、10、11
				String[] tempStr2Array = tempStr2.split(" ");
				int[] statesArray = new int[tempStr2Array.length];//存放节点及其父节点取值个数的集合
				for(int i = 0; i < tempStr2Array.length; i++) {
					statesArray[i] = Integer.parseInt(tempStr2Array[i]);
				}
				String[] tempStr3Array = tempStr3.split(" ");
				float[] cptArray = new float[tempStr3Array.length];//存放节点的参数表
				for(int i = 0; i < tempStr3Array.length; i++) {
					cptArray[i] = Float.parseFloat(tempStr3Array[i]);
				}
				BaseVertex aVertex = new BaseVertex(tempStr1, statesArray, cptArray);//创建节点对象
				aVertex.setID(this.vertexId);//将文件中节点的存放次序设置为节点编号属性
				this.vertexList.add(aVertex);//将节点存入节点集vertexList中，细节：节点的编号等于节点集合vertexList的下标
				this.vertexMap.put(tempStr1, new Integer(this.vertexId));//将节点名称与节点编号对应存入Map对象中
				this.vertexId++;//节点编号加一
				tempStr1 = nodebr.readLine();//获取下一个节点
			}
			nodebr.close();
			BufferedReader edgebr = new BufferedReader(new FileReader(edgefilepath));
			tempStr1 = edgebr.readLine();//读取有向边文本edges.txt的第一行内容，即节点及其父节点
			while(tempStr1 != null) {
				String[] edgeStrArray = tempStr1.split(" ");
				int edgeNum = edgeStrArray.length;//edgeNum存放边的数量+1
				int headVextexID = this.vertexMap.get(edgeStrArray[0]).intValue();//headVextexID存放子节点的编号
				int tailVertexID = -1;//tailVertexID存放父节点的编号，初始值为-1
				BaseVertex headVertex = this.vertexList.get(headVextexID);//获取编号为headVextexID的子节点，注意节点存入节点集合vertexList的次序与节点的编号一致
				BaseVertex tailVertex = null;//tailVertex存放父节点
				BaseEdge headPEdge = null;//headPEdge存放入度边
				BaseEdge tailPEdge = null;//tailPEdge存放出度边
				for(int i = 1; i < edgeNum; i++) {
					tailVertexID = this.vertexMap.get(edgeStrArray[i]).intValue();//tailVertexID存放父节点的编号
					tailVertex = this.vertexList.get(tailVertexID);//tailVertex存放父节点
					BaseEdge anEdge = new BaseEdge(edgeStrArray[i] + "->" + edgeStrArray[0]);//创建从节点edgeStrArray[i]到节点edgeStrArray[0]的有向边对象anEdge
					anEdge.setTailVertexID(tailVertexID);//设置父节点的编号tailVertexID
					anEdge.setHeadVertexID(headVextexID);//设置子节点的编号headVextexID
					tailVertex.incOutDegree();//父节点的出度加一
					headVertex.incInDegree();//子节点的入度加一
					if(headVertex.getHeadLink() == null) {
						headVertex.setHeadLink(anEdge);//设置子节点的第一条入度边
						headPEdge = anEdge;//headPEdge存放入度边
					}else {
						headPEdge.setHeadNext(anEdge);//设置子节点的下一条入度边
						headPEdge = anEdge;
					}
					if(tailVertex.getTailLink() == null) {
						tailVertex.setTailLink(anEdge);//设置父节点的第一条出度边
					}else {
						tailPEdge = tailVertex.getTailLink();//获取父节点的第一条出度边
						while(tailPEdge.getTailNext() != null) {//获取父节点的下一条出度边，直到下一条出度边为null
							tailPEdge = tailPEdge.getTailNext();
						}
						tailPEdge.setTailNext(anEdge);//设置anEdge为出度边的下一条出度边
					}
				}
				tempStr1 = edgebr.readLine();//获取下一个节点对应的边
			}
			edgebr.close();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 节点编号的get方法
	 * @return 节点编号
	 */
	public int getVertexId() {
		return this.vertexId;
	}
	
	/**
	 * 节点列表的get方法
	 * @return 节点列表
	 */
	public ArrayList<BaseVertex> getVertexList() {
		return this.vertexList;
	}
	
	/**
	 * 根据节点编号（节点列表下标）获得节点
	 * @param id 节点编号
	 * @return 节点
	 */
	public BaseVertex getVertexWithID(int id) {
		return this.getVertexList().get(id);
	}
	
	/**
	 * 根据节点名称，查询map数组获得节点编号（节点列表下标），进而获得节点
	 * @param label 节点名称
	 * @return 节点
	 */
	public BaseVertex getVertexWithLabel(String label) {
		int vertexID = this.vertexMap.get(label);
		return this.getVertexList().get(vertexID);
	}
	
	/**
	 * 获得节点当前状态的条件概率
	 * @param vertexID 节点编号
	 * @return 节点当前状态的条件概率
	 */
	public float getCurrentStatusConProbability(int vertexID) {
		BaseVertex vertex = this.vertexList.get(vertexID);
		String statusStr = "" + vertex.getStatus();
		BaseEdge pEdge = vertex.getHeadLink();
		while(pEdge != null) {
			statusStr += this.vertexList.get(pEdge.getTailVertexID()).getStatus();
			pEdge = pEdge.getHeadNext();
		}
		float vertexProbability = vertex.getConditionalProbability(statusStr);
		return vertexProbability;//获得节点vertex的条件概率
	}
	
	/**
	 * 证据节点的set方法
	 * @param evidenceMap 存放证据节点的Map对象
	 * @return 证据节点的个数
	 */
	public int setEvidenceVertex(Map<String, Integer> evidenceMap) {
		int evidenceNum = 0;//存放证据节点的个数
		//同时获取Map对象中key、value的一种方式
		Iterator<Entry<String, Integer>> iter = evidenceMap.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iter.next();
			String vertexLabel = entry.getKey();//获取节点名称，Map中的key
			int vertexStatus = entry.getValue().intValue();//获取节点状态，Map中的value
			BaseVertex avertex = this.getVertexWithLabel(vertexLabel);//根据节点名称获得节点
			avertex.setStatus(vertexStatus);//设置节点状态
			avertex.setIsEvidence(true);//设置节点为证据节点
			evidenceNum++;
		}
		return evidenceNum;
	}
}
