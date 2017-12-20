package 包名;

/**
 * 边实体类
 * @author 作者名
 */
public class BaseEdge {

	private String label;//边的名称，例如"A->B", 默认"Default"
	private int tailVertexID;//父节点A的编号，默认-1
	private int headVertexID;//子节点B的编号，默认-1
	private BaseEdge tailNext;//父节点A的下一条出度边
	private BaseEdge headNext;//子节点B的下一条入度边
	
	/**
	 * 无输入的构造函数
	 */
	public BaseEdge() {
		this.label = "Default";
		this.tailVertexID = -1;
		this.headVertexID = -1;
		this.tailNext = null;
		this.headNext = null;
	}

	/**
	 * 输入边名称的构造函数
	 * @param label
	 */
	public BaseEdge(String label) {
		this.label = label;
		this.tailVertexID = -1;
		this.headVertexID = -1;
		this.tailNext = null;
		this.headNext = null;
	}
	
	/**
	 * 边名称的set方法
	 * @param label 待赋值的名称
	 * @return 赋值的结果true/false
	 */
	public boolean setLabel(String label) {
		if(label == null)
			return false;
		else
			this.label = label;
		return true;
	}
	
	/**
	 * 边名称的get方法
	 * @return 边的名称
	 */
	public String getLabel() {
		return this.label;
	}
	
	/**
	 * 父节点编号的set方法
	 * @param tailVertexID 待赋值的编号
	 * @return 赋值的结果
	 */
	public boolean setTailVertexID(int tailVertexID) {
		if(tailVertexID < 0)
			return false;
		else
			this.tailVertexID = tailVertexID;
		return true;
	}
	
	/**
	 * 父节点编号的get方法
	 * @return 父节点编号
	 */
	public int getTailVertexID() {
		return this.tailVertexID;
	}
	
	/**
	 * 子节点编号的set方法
	 * @param headVertexID 待赋值的编号
	 * @return 赋值的结果
	 */
	public boolean setHeadVertexID(int headVertexID) {
		if(headVertexID < 0)
			return false;
		else
			this.headVertexID = headVertexID;
		return true;
	}
	
	/**
	 * 子节点编号的get方法
	 * @return 子节点编号
	 */
	public int getHeadVertexID() {
		return this.headVertexID;
	}
	
	/**
	 * 父节点下一条出度边的set方法
	 * @param tailNext 待赋值的出度边
	 * @return 赋值的结果
	 */
	public boolean setTailNext(BaseEdge tailNext) {
		if(tailNext == null)
			return false;
		else
			this.tailNext = tailNext;
		return true;
	}
	
	/**
	 * 父节点下一条出度边的get方法
	 * @return 父节点的下一条出度边
	 */
	public BaseEdge getTailNext() {
		return this.tailNext;
	}
	
	/**
	 * 子节点下一条入度边的set方法
	 * @param headNext 待赋值的入度边
	 * @return 赋值的结果
	 */
	public boolean setHeadNext(BaseEdge headNext) {
		if(headNext == null)
			return false;
		else
			this.headNext = headNext;
		return true;
	}
	
	/**
	 * 子节点下一条入度边的get方法
	 * @return 子节点的下一条入度边
	 */
	public BaseEdge getHeadNext() {
		return this.headNext;
	}
	
	/**
	 * 下一条入度、出度边的set方法
	 * @param tailNext 待赋值的出度边
	 * @param headNext 待赋值的入度边
	 * @return 赋值的结果
	 */
	public boolean setTailAndHeadNext(BaseEdge tailNext, BaseEdge headNext) {
		boolean bool1 = this.setTailNext(tailNext);
		boolean bool2 = this.setHeadNext(headNext);
		return bool1 && bool2;
	}
	
	/**
	 * 边信息的toString方法
	 */
	public String toString() {
		return "[" + this.label + " ID:" + this.tailVertexID + "->" + this.headVertexID + "]";
	}
}
