# rejection sampling

拒绝采样算法，又称为接受拒绝采样算法、逻辑抽样、舍选抽样，java实现。

* 从分布P(X)中抽取样本，采用顺序抽样(forward sampling)，借助随机数产生器(random number generator)。按照贝叶斯网的拓扑序对其中的变量逐个进行抽样：对待抽样变量X, 若它是根节点，则按分布P(X)进行抽样；若是非根节点，则按分布P(X|π(X)=r)进行抽样，这里π(X)=r是X的父节点的抽样结果，在对X抽样时是已知的。
* 假设通过顺序抽样获得m个独立样本，其中满足证据E=e的有n个，而在这n个样本中，进一步满足Q=q的有p个。
* 逻辑抽样法获得的对后验概率的近似为P(Q=q|E=e)≈p/n, 它是在所有满足E=e的样本中，进一步满足Q=q的样本所占的比例，逻辑抽样法所产生与证据E=e不一致的那些样本被舍弃。
  
算法伪代码：<br />
**function** REJECTION-SAMPLING(X, **e**, bn, N) **returns** an estimate of P(X|**e**)<br />
&emsp;**inputs**: X, the query variable<br />
&emsp;&emsp;&emsp;&emsp;&nbsp;**e**, evidence specified as an event<br />
&emsp;&emsp;&emsp;&emsp;&nbsp;bn, a Bayesian network<br />
&emsp;&emsp;&emsp;&emsp;&nbsp;N, the total number of samples to be generated<br />
&emsp;**local variables**: **N**, a vector of counts over X, initially zero<br /><br />
&emsp;**for** j = 1 to N **do**<br />
&emsp;&emsp;**x** <- PRIOR-SAMPLE(bn)<br />
&emsp;&emsp;**if x** is consistent with **e then**<br />
&emsp;&emsp;&emsp;**N**[x] <- **N**[x]+1 where x is the value of X in **x**<br />
&emsp;**return** NORMALIZE(**N**[X])

> 算法优点：简单易行。<br />
> 算法缺点：当概率P(E=e)很小时，算法效率低，收敛速度慢。随着P(E=e)的减小，所抽的的与E=e一致的样本个数将会减少，因此大量样本被舍弃，造成计算资源的浪费。
