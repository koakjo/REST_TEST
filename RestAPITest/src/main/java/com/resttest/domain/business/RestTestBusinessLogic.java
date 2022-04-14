package com.resttest.domain.business;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.RollbackException;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.resttest.domain.entity.REST_TEST;
import com.resttest.domain.entity.REST_TEST_PK;
import com.resttest.infra.repository.RestTestResource;

import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Dependent
public class RestTestBusinessLogic {
	
	@PostConstruct
	public void construct() {
		//特に何もしない
	}
	
	@Inject
	RestTestResource resource;
	
	public String getName(String id) throws Exception{
		
		REST_TEST_PK pkEntity = new REST_TEST_PK();
		pkEntity.setId(id);
		REST_TEST entity = resource.findById(pkEntity);
		
		if (entity.getName().equals("")) {
			return "no records";
		} else {
			return entity.getName();
		}
	}
	
	@Transactional
	public String getTimeOutException(String id) throws Exception{
		
		//100秒のスリープ
		Thread.sleep(100000);;
		
		return id;
	}
	
	
	/*
	 * ここよりより20220405-transaction検証用コード*******************************************************************************************************
	 */
	
	
	/*
	 * SOA処理に見立てた処理を使用するテストメソッド
	 * 1. SOAに見立てた処理メソッド1つ、DBアクセス前にSOAに見立てた処理、DBアクセス有り
	 */
	@Transactional
	public String testSOAmethod1() throws Exception{
		
		try {
			//SOAに見立てた100秒のスリープ
			try {
				this.getSleepLikeSOA100();
			} catch (Exception e ) {
				e.printStackTrace();
				return "testSOAmethod1_sleep_failure_BusinessLogic";
			}
			//DBアクセス
			String outcome = this.getDBAccess() + ":testSOAmethod1";
			
			//結果リターン
			return outcome;
			
		} catch (RollbackException re) {
			System.out.println("testSOAmethod1_RollbackException_BusinessLogic");
			throw re;
		} catch (Exception e) {
			System.out.println("testSOAmethod1_Exception_BusinessLogic");
			throw e;
		}
	}
	
	/*
	 * SOA処理に見立てた処理を使用するテストメソッド
	 * 2. SOAに見立てた処理メソッド1つ、DBアクセス後にSOAに見立てた処理、DBアクセス有り
	 */
	@Transactional(value = TxType.REQUIRES_NEW)
	public String testSOAmethod2() throws Exception{
		
		try {
			//DBアクセス
			String outcome = this.getDBAccess() + ":testSOAmethod2";
			
			//SOAに見立てた100秒のスリープ
			try {
				this.getSleepLikeSOA100();
			} catch (Exception e ) {
				e.printStackTrace();
				return "testSOAmethod2_sleep_failure_BusinessLogic";
			}
			
			//結果リターン
			return outcome;
			
		} catch (RollbackException re) {
			System.out.println("testSOAmethod2_RollbackException_BusinessLogic");
			throw re;
		} catch (Exception e) {
			System.out.println("testSOAmethod2_Exception_BusinessLogic");
			throw e;
		}
	}
	/*
	 * SOA処理に見立てた処理を使用するテストメソッド
	 * 3. DBアクセス時にスリープ
	 */
	@Transactional
	public String testSOAmethod3() throws Exception{
		
		try {
			//DBアクセス
			this.getDBSleep();
			
			//結果リターン
			return "testSOAmethod3";
			
		} catch (RollbackException re) {
			System.out.println("testSOAmethod3_RollbackException_BusinessLogic");
			throw re;
		} catch (Exception e) {
			System.out.println("testSOAmethod3_Exception_BusinessLogic");
			throw e;
		}
	}
	
	/*
	 * SOA処理に見立てた処理を使用するテストメソッド
	 * なお20220401検証と内容同じなため割愛
	 * 4. SOAに見立てた処理メソッド1つ、、DBアクセスなし
	 */
	@Transactional
	public String testSOAmethod4() throws Exception{
		
		try {
			//DBアクセス
			String outcome = "testSOAmethod4";
			
			//SOAに見立てた100秒のスリープ
			try {
				this.getSleepLikeSOA100();
			} catch (Exception e ) {
				e.printStackTrace();
				return "testSOAmethod4_sleep_failure_BusinessLogic";
			}
			
			//結果リターン
			return outcome;
			
		} catch (Exception e) {
			System.out.println("testSOAmethod4_Exception_BusinessLogic");
			throw e;
		}
	}
	
	/*
	 * SOA処理に見立てた処理を使用するテストメソッド
	 * 5. SOAに見立てた処理メソッド2つ、DBアクセス前にSOAに見立てた処理、DBアクセス有り
	 */
	@Transactional
	public String testSOAmethod5() throws Exception{
		
		try {
			//DBアクセス
			String outcome = this.getDBAccess() + ":testSOAmethod5";
			
			//SOA①に見立てた100秒のスリープ
			try {
				this.getSleepLikeSOA100();
			} catch (Exception e ) {
				e.printStackTrace();
				return "testSOAmethod5_sleep1_failure_BusinessLogic";
			}
			
			//SOA②に見立てた100秒のスリープ
			try {
				this.getSleepLikeSOA100();
			} catch (Exception e ) {
				e.printStackTrace();
				return "testSOAmethod5_sleep2_failure_BusinessLogic";
			}
			
			//結果リターン
			return outcome;
			
		} catch (RollbackException re) {
			System.out.println("testSOAmethod5_RollbackException_BusinessLogic");
			throw re;
		} catch (Exception e) {
			System.out.println("testSOAmethod5_Exception_BusinessLogic");
			throw e;
		}
	}
	
	/*
	 * SOA処理に見立てた処理を使用するテストメソッド
	 * 6. SOAに見立てた処理メソッド2つ、DBアクセスをSOAに見立てた処理1と2の間、DBアクセス有り
	 */
	@Transactional
	public String testSOAmethod6() throws Exception{
		
		try {
			
			try {
				this.getSleepLikeSOA100();
			} catch (Exception e ) {
				e.printStackTrace();
				return "testSOAmethod6_sleep1_failure_BusinessLogic";
			}
			//DBアクセス
			String outcome = this.getDBAccess() + ":testSOAmethod6";
			
			//SOAに見立てた100秒のスリープ
			try {
				this.getSleepLikeSOA100();
			} catch (Exception e ) {
				e.printStackTrace();
				return "testSOAmethod6_sleep2_failure_BusinessLogic";
			}
			
			//結果リターン
			return outcome;
			
		} catch (RollbackException re) {
			System.out.println("testSOAmethod6_RollbackException_BusinessLogic");
			throw re;
		} catch (Exception e) {
			System.out.println("testSOAmethod6_Exception_BusinessLogic");
			throw e;
		}
	}
	
	/*
	 * SOA処理に見立てた処理を使用するテストメソッド
	 * 7. SOAに見立てた処理メソッド2つ、DBアクセス前にSOAに見立てた処理、DBアクセス有り
	 */
	@Transactional
	public String testSOAmethod7() throws Exception{
		
		try {
			
			//DBアクセス
			String outcome = this.getDBAccess() + ":testSOAmethod7";
			
			//SOAに見立てた100秒のスリープ1
			try {
				this.getSleepLikeSOA100();
			} catch (Exception e ) {
				e.printStackTrace();
				return "testSOAmethod7_sleep1_failure_BusinessLogic";
			}
			
			//SOAに見立てた100秒のスリープ2
			try {
				this.getSleepLikeSOA100();
			} catch (Exception e ) {
				e.printStackTrace();
				return "testSOAmethod7_sleep2_failure_BusinessLogic";
			}
			
			//結果リターン
			return outcome;
			
		} catch (RollbackException re) {
			System.out.println("testSOAmethod7_RollbackException_BusinessLogic");
			throw re;
		} catch (Exception e) {
			System.out.println("testSOAmethod7_Exception_BusinessLogic");
			throw e;
		}
	}
	
	/*
	 * UserTransactionを使用したテスト
	 * 8. UserTransactionとdefault-timeoutを同時間、DBアクセスあり
	 */
	public String testUserTransaction8() throws Exception{
		
		try {
			//結果取得
			String outcome = this.getDBAccessUtx(60, 60) + ":testUserTransaction8";
			
			//メソッド終了
			return outcome;
			
		} catch (RollbackException re) {
			System.out.println("testUserTransaction8_RollbackException_BusinessLogic");
			throw re;
		} catch (Exception e) {
			System.out.println("testUserTransaction8_Exception_BusinessLogic");
			throw e;
		}
	}
	
	/*
	 * UserTransactionを使用したテスト
	 * 9. UserTransactionとdefault-timeoutを同時間、DBアクセスなし
	 */
	public String testUserTransaction9() throws Exception{
		
		try {
			
			//結果取得
			String outcome = "testUserTransaction9";
			
			//selectしないでタイムアウトと同じ時間スリープ
			this.getDBOnlyAccessUtx(60, 60);
			
			//メソッド終了
			return outcome;
		
		} catch (RollbackException re) {
			System.out.println("testUserTransaction9_RollbackException_BusinessLogic");
			throw re;
		} catch (Exception e) {
			System.out.println("testUserTransaction9_Exception_BusinessLogic");
			throw e;
		}
	}
	
	/*
	 * UserTransactionを使用したテスト
	 * 10. UserTransaction、default-timeoutより長い時間、DBアクセスあり
	 */
	public String testUserTransaction10() throws Exception{
		
		try {
			//結果取得
			String outcome = this.getDBAccessUtx(60, 100) + ":testUserTransaction10";
			
			//メソッド終了
			return outcome;
			
		} catch (RollbackException re) {
			System.out.println("testUserTransaction10_RollbackException_BusinessLogic");
			throw re;
		} catch (Exception e) {
			System.out.println("testUserTransaction10_Exception_BusinessLogic");
			throw e;
		}
	}
	
	/*
	 * UserTransactionを使用したテスト
	 * 11. UserTransaction、default-timeoutより長い時間、DBアクセスなし
	 */
	public String testUserTransaction11() throws Exception{
		
		try {
			
			//結果取得
			String outcome = "testUserTransaction11";
			
			//selectしないでタイムアウトより長い時間スリープ
			this.getDBOnlyAccessUtx(60, 100);
			
			//メソッド終了
			return outcome;
		
		} catch (RollbackException re) {
			System.out.println("testUserTransaction11_RollbackException_BusinessLogic");
			throw re;
		} catch (Exception e) {
			System.out.println("testUserTransaction11_Exception_BusinessLogic");
			throw e;
		}
	}
	
	/*
	 * UserTransactionを使用したテスト
	 * 12. UserTransaction、default-timeoutより短い時間、DBアクセスあり
	 */
	public String testUserTransaction12() throws Exception{
		
		try {
			//結果取得
			String outcome = this.getDBAccessUtx(100, 60) + ":testUserTransaction12";
			
			//メソッド終了
			return outcome;
			
		} catch (RollbackException re) {
			System.out.println("testUserTransaction12_RollbackException_BusinessLogic");
			throw re;
		} catch (Exception e) {
			System.out.println("testUserTransaction12_Exception_BusinessLogic");
			throw e;
		}
	}
	
	/*
	 * UserTransactionを使用したテスト
	 * 11. UserTransactionとdefault-timeoutより短い時間、DBアクセスなし
	 */
	public String testUserTransaction13() throws Exception{
		
		try {
			
			//結果取得
			String outcome = "testUserTransaction13";
			
			//selectしないでタイムアウトより長い時間スリープ
			this.getDBOnlyAccessUtx(100, 60);
			
			//メソッド終了
			return outcome;
		
		} catch (RollbackException re) {
			System.out.println("testUserTransaction13_RollbackException_BusinessLogic");
			throw re;
		} catch (Exception e) {
			System.out.println("testUserTransaction13_Exception_BusinessLogic");
			throw e;
		}
	}
	
	/*
	 * TimeLimiterを使用したテスト
	 * 別スレッドで対象のメソッドを呼ぶ形
	 */
	public String testTimeLimiter14() {
		
		try {
			
			ExecutorService threadPool = Executors.newCachedThreadPool();
			
			/*
			 * configによって制御する
			 * 今回は10秒を設定する
			 */
			TimeLimiterConfig config = TimeLimiterConfig.custom()
			        .timeoutDuration(Duration.ofMillis(10000))
			        .cancelRunningFuture(true)
			        .build();
			
			/*
			 * callableでtestSOAmethod1を設定、同時にTimeLimiterによる時間制御を入れる。
			 * 指定時間より長くなってしまった場合はtestSOAmethod1を実行しているThreadを殺すような形。
			 * どんな場合にどのような例外を発出するのかはよく仕様を確認する必要がある
			 * ex1. callableで呼び出したメソッドに対し、@Transactionalを使っている場合はThreadが殺された場合にtxをrollbackするのかなど
			 *      →これはjbossのロガーを全て出力する設定にして確認する必要あり
			 * ex2. そもそもマルチスレッドでやっていいものか、またResilience4jにマルチスレッド処理を任せていいのか。
			 *      というか自前実装してもできるのでは？（OSSライブラリとはいえプロダクトに使っていいものか？）
			 *      →マルチスレッド前提でない認識。大丈夫？
			 * ex3. ここまでしてタイマー処理を行うニーズがあるか？
			 *      →ない？運用によって制御できるのであれば大丈夫だが。展開とか考えた時にTimeLimiter使ってていいのかね、は疑問が残る
			 */
			Callable<String> callable = TimeLimiter.decorateFutureSupplier(TimeLimiter.of(config),() -> threadPool.submit(() -> testSOAmethod1()));
			
			/*
			 * ここで指定した処理を呼び出せる
			 * 別スレッドにて呼び出し先の処理を行い、
			 * 呼び出し元（つまりcallable.call();をしたスレッド）にてタイマー監視をし、時間になったら呼び出し先のthreadを殺す形。
			 */
			callable.call();
			
			
			return "testTimeLimiter14-testSOAmethod1";
			
		//TimeLimiterが発出するTimeoutExceptionはここでキャッチできる
		} catch (TimeoutException te) {
			te.printStackTrace();
			return "testTimeLimiter14 timeout-exception occuered";
		
		}catch (Exception e) {
			e.printStackTrace();
			return "testTimeLimiter14 exception occuered";
		}
		
	}
	
	/*
	 * REST API をコールするメソッド
	 */
	public String testRestMethod15(String id) throws Exception{
		
		REST_TEST_PK pkEntity = new REST_TEST_PK();
		pkEntity.setId(id);
		REST_TEST restTestEntity = resource.findById(pkEntity);
		return restTestEntity.getName();
		
	}
	
	/*
	 * REST API をコールするメソッド
	 */
	public String testRestMethod16(String id)  throws Exception{
		
		REST_TEST_PK pkEntity = new REST_TEST_PK();
		pkEntity.setId(id);
		REST_TEST restTestEntity = resource.findById(pkEntity);
		return restTestEntity.getName();
		
	}
	
	/*
	 * REST API をコールするメソッド
	 */
	public String testRestMethod17(String id)  throws Exception{
		
		REST_TEST_PK pkEntity = new REST_TEST_PK();
		pkEntity.setId(id);
		REST_TEST restTestEntity = resource.findById(pkEntity);
		return restTestEntity.getName();
		
	}
	
	/*
	 * ここから先はPrivateMethod **********************************************************************************************
	 */
	
	//各テストメソッドから呼ばれるDBアクセスプライベートメソッド
	//"BLUE HAT AMATEUR LINUX"をDBアクセスの上、リターンする
	private String getDBAccess() throws Exception{
		System.out.println("***********getDBAccess started***********");
		REST_TEST_PK pkEntity = new REST_TEST_PK();
		pkEntity.setId("5");
		REST_TEST entity = resource.findById(pkEntity);
		return entity.getName();
	}
	
	//各テストメソッドから呼ばれるDBアクセスプライベートメソッド
	//"BLUE HAT AMATEUR LINUX"をDBアクセスの上、リターンする
	//引数にてtxのトランザクションタイムアウト秒数を設定する（UserTransactionにて設定する。）
	private String getDBAccessUtx(int timeoutSec , int txTimeoutSec) throws Exception{
		System.out.println("***********getDBAccessUtx started***********");
		REST_TEST_PK pkEntity = new REST_TEST_PK();
		pkEntity.setId("5");
		REST_TEST entity = resource.findByIdUtxWithTimeout(pkEntity,timeoutSec , txTimeoutSec);
		return entity.getName();
	}
	
	//DBアクセスなしにTx張り、SQL発行なくDBアクセスを終了する
	private void getDBOnlyAccessUtx(int timeoutSec, int txTimeoutSec) throws Exception{
		System.out.println("***********getDBOnlyAccessUtx started***********");
		resource.onlyUtx(timeoutSec, txTimeoutSec);
	}
	
	//DBアクセス時にスリープ処理を行うメソッド
	private void getDBSleep() throws Exception{
		System.out.println("***********getDBSleep started***********");
		resource.nativeQueryExecute("SELECT pg_sleep(100);");
	}
	
	//SOA処理に見立てたメソッド
	//sleep処理を行う。100秒
	private void getSleepLikeSOA100() throws Exception{
		System.out.println("***********getSleepLikeSOA100 started***********");
		Thread.sleep(100000);
	}
	
}
