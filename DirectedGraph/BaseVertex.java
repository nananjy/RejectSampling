package 包名;

/**
 * 节点实体类
 * @author 作者名
 */
public class BaseVertex {

	private int id;//节点编号
	private String label;//节点名称
	private BaseEdge tailLink;//节点的第一条出度边
	private BaseEdge headLink;//节点的第一条入度边
	private int inDegree;//入度
	private int outDegree;//出度
	private int statesNum;//父节点的个数+1，默认为1
	private int[] states;//父节点可以取值状态的个数，默认为2
	private int status;//节点状态0/1，默认为-1
	private float[] cpt;//节点的参数表
	private boolean isEvidence;//节点为证据节点，取值true/false
	
	/**
	 * 构造函数
	 */
	public BaseVertex() {
		this.id = -1;
		this.label = "Default";
		this.tailLink = null;
		this.headLink = null;
		this.inDegree = 0;
		this.outDegree = 0;
		this.statesNum = 1;
		this.states = new int[1];
		this.states[0] = 2;
		this.status = -1;
		this.cpt = new float[2];
		this.cpt[0] = 0.5f;
		this.cpt[1] = 0.5f;
		this.isEvidence = false;
	}
	
	/**
	 * 输入节点名称的构造函数
	 * @param label 节点名称
	 */
	public BaseVertex(String label) {
		this.id = -1;
		this.label = label;
		this.tailLink = null;
		this.headLink = null;
		this.inDegree = 0;
		this.outDegree = 0;
		this.statesNum = 1;
		this.states = new int[1];
		this.states[0] = 2;
		this.status = -1;
		this.cpt = new float[2];
		this.cpt[0] = 0.5f;
		this.cpt[1] = 0.5f;
		this.isEvidence = false;
	}
	
	/**
	 * 输入节点名称、节点及父节点取值个数、参数表的构造函数
	 * @param label 节点名称
	 * @param states 节点及父节点取值个数
	 * @param cpt 参数表
	 */
	public BaseVertex(String label, int[] states, float[] cpt) {
		this.id = -1;
		this.label = label;
		this.tailLink = null;
		this.headLink = null;
		this.inDegree = 0;
		this.outDegree = 0;
		this.statesNum = states.length;//父节点个数+1
		this.states = states;
		this.status = 0;//节点取值状态
		this.cpt = cpt;
		this.isEvidence = false;
	}
	
	/**
	 * 节点编号的set方法
	 * @param id 待赋值的编号
	 * @return 赋值的结果true/false
	 */
	public boolean setID(int id) {
		if(id<0)
			return false;
		else
			this.id = id;
		return true;
	}
	
	/**
	 * 节点编号的get方法
	 * @return 节点编号
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * 节点名称的set方法
	 * @param label 待赋值的名称
	 * @return 赋值的结果true or false
	 */
	public boolean setLabel(String label) {
		if(label == null)
			return false;
		else
			this.label = label;
		return true;
	}
	
	/**
	 * 节点名称的get方法
	 * @return 节点名称
	 */
	public String getLabel() {
		return this.label;
	}
	
	/**
	 * 节点第一条出度边的set方法
	 * @param tailLink 待赋值的出度边
	 * @return 赋值的结果true/false
	 */
	public boolean setTailLink(BaseEdge tailLink) {
		if(tailLink == null)
			return false;
		else
			this.tailLink = tailLink;
		return true;
	}
	
	/**
	 * 节点第一条出度边的get方法
	 * @return 节点的第一条出度边
	 */
	public BaseEdge getTailLink() {
		return this.tailLink;
	}
	
	/**
	 * 节点第一条入度边的set方法
	 * @param headLink 待赋值的入度边
	 * @return 赋值的结果true/false
	 */
	public boolean setHeadLink(BaseEdge headLink) {
		if(headLink == null)
			return false;
		else
			this.headLink = headLink;
		return true;
	}
	
	/**
	 * 节点第一条入度边的get方法
	 * @return 节点的第一条入度边
	 */
	public BaseEdge getHeadLink() {
		return this.headLink;
	}
	
	/**
	 * 节点第一条入度、出度边的set方法
	 * @param tailLink 待赋值的出度边
	 * @param headLink 待赋值的入读边
	 * @return 赋值的结果
	 */
	public boolean setHeadAndTailLink(BaseEdge tailLink, BaseEdge headLink) {
		boolean bool1 = this.setTailLink(tailLink);
		boolean bool2 = this.setHeadLink(headLink);
		return bool1 && bool2;
	}
	
	/**
	 * 节点入度的get方法
	 * @return 入度
	 */
	public int getInDegree() {
		return this.inDegree;
	}
	
	/**
	 * 节点出度的get方法
	 * @return 出度
	 */
	public int getOutDegree() {
		return this.outDegree;
	}
	
	/**
	 * 节点入度加一
	 * @return 入度
	 */
	public int incInDegree() {
		this.inDegree++;
		return this.inDegree;
	}
	
	/**
	 * 节点出度加一
	 * @return 出度
	 */
	public int incOutDegree() {
		this.outDegree++;
		return this.outDegree;
	}
	
	/**
	 * 节点入度减一
	 * @return 入度
	 */
	public int decInDegree() {
		this.inDegree--;
		return this.inDegree;
	}
	
	/**
	 * 节点出度减一
	 * @return 出度
	 */
	public int decOutDegree() {
		this.outDegree--;
		return this.outDegree;
	}
	
	/**
	 * 父节点个数+1的get方法
	 * @return 父节点个数+1的get方法
	 */
	public int getStatesNum() {
		return this.statesNum;
	}
	
	/**
	 * 节点状态的get方法
	 * @return 节点状态，一般为0/1
	 */
	public int getStatus() {
		return this.status;
	}
	
	/**
	 * 节点状态的set方法
	 * @param status 待赋值的状态
	 * @return 赋值的结果true/false
	 */
	public boolean setStatus(int status) {
		if(status < 0)
			return false;
		else
			this.status = status;
		return true;
	}
	
	/**
	 * 节点为证据节点的get方法
	 * @return 节点为证据节点true/false
	 */
	public boolean getIsEvidence() {
		return this.isEvidence;
	}
	
	/**
	 * 节点为证据节点的set方法
	 * @param isEvidence 待赋值的证据节点值
	 * @return 节点为证据节点true/false
	 */
	public boolean setIsEvidence(boolean isEvidence) {
		this.isEvidence = isEvidence;
		return this.isEvidence;
	}
	
	/**
	 * 获得节点最后一个父节点的状态个数，一般为2
	 * @param order 父节点个数
	 * @return 最后一个父节点可能取值的个数
	 */
	public int getStatesOfOrder(int order) {
		if((order >= this.statesNum) || (order < 0))
			return -1;
		else
			return this.states[order];
	}
	
	/**
	 * 根据参数表，获得节点在对应状态statusStr下的条件概率
	 * @param statusStr 节点及父节点的状态
	 * @return 参数表对应的条件概率
	 */
	public float getConditionalProbability(String statusStr) {
		String[] sstr = statusStr.split("");
		int statusLength = sstr.length;//存放状态字符串的位数
		if(statusLength != this.statesNum)//判断节点及其父节点个数与其对应的状态个数是否一致
			return -1;
		int tempStatus = -1;//存放节点状态
		int index = 0;//存放参数表的索引
		for(int i = 0; i < statusLength; i++){
			tempStatus = Integer.parseInt(sstr[i]);
			if(tempStatus >= this.states[this.statesNum-1-i])//判断节点状态(0/1)是否大于等于第this.statesNum-1-i个节点的取值个数
				return -1;
			if (tempStatus != 0) {//判断节点状态是否为0，若不为0，计算参数表索引
				for(int j = 0; j < this.statesNum-1-i; j++)
					tempStatus = tempStatus*this.states[j];
				index = index + tempStatus;
			}
		}
		if(index >= this.cpt.length)
			return -1;
		return this.cpt[index];
	}
	
	/**
	 * 获得节点信息的toString方法
	 */
	public String toString() {
		return "(Label:" + this.label + " InDegree:" + this.inDegree + " OutDegree:" + this.outDegree + " Status:" + this.status + ")";
	}
}
