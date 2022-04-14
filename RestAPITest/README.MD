テストアプリケーションです。
オーナーが今後、実装を検討する際はこのアプリケーションをいじって検証して進めます。

[テスト環境]
OS→IntelMac OS BigSur
APサーバ→WildFly 22.0.1
DB→Postgresql@13[brew install postgresql@13]

[環境構築手順]
[PostgreSQL]

--以下で不明の場合、オーナーまでお問い合わせください。時間のある時に対応いたします。

--login as superuser to default db.
psql -d postgres;

CREATE DATABASE REST_TEST;
CREATE USER RESTUSER;

GRANT ALL ON ALL TABLES IN SCHEMA public TO restuser;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO restuser;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA public TO restuser;

--password setting
\password restuser
rest
rest

exit;
--psql exit

--login as restuser to db.
psql -d rest_test -U restuser;

--create table
CREATE TABLE REST_TEST_MASTER
(id                 CHAR(10)    ,
name                CHAR(30)    ,
PRIMARY KEY(id));

INSERT INTO REST_TEST_MASTER VALUES('1','RESTless API');
INSERT INTO REST_TEST_MASTER VALUES('2','DIRTYSOAP');
INSERT INTO REST_TEST_MASTER VALUES('3','HTML007');
INSERT INTO REST_TEST_MASTER VALUES('4','HTTPSSS');
INSERT INTO REST_TEST_MASTER VALUES('5','BLUE HAT AMATEUR LINUX');
INSERT INTO REST_TEST_MASTER VALUES('6','FAT DARWIN');
INSERT INTO REST_TEST_MASTER VALUES('7','ELEGANTFLY');
INSERT INTO REST_TEST_MASTER VALUES('8','CLOSEDSHIFT');





[WildFly]
①WildFly22.0.1 finalを以下から入手、解答します

https://www.wildfly.org/downloads/
(Jakarta EE Full & Web Distributionのzip)

②以下のサイトを参考にユーザの追加、及びデータソースの追加設定をします。
DataSourceの設定時は、[PostgreSQL]での設定をそのまま流用いただけると良いと思います。
https://qiita.com/tama1/items/829be5aacd81637ae73a

もしくは、当プロジェクトの[WildFly-settings]フォルダの配下をそれぞれ適切な場所に配置します

なお、テストアプリの設定は以下になっています
DataSource名→java:PostgresDS

③設定が完了したら、以下のシェルを実行します。

$JBOSS_HOME/bin/standalone.sh

[疎通確認]
WildFlyを実行している状態で、HTTP経由で行います。
ポート等はデフォルトを想定します。

[GET]
以下をブラウザにて実行ください。
http://127.0.0.1:8080/RestTestApp/Rest/Test/hello

http://127.0.0.1:8080/RestClient/test/method15

"HELLO"みたいな文字列が帰ってきます。

[POST]
以下をターミナルにて実行ください。

curl -X POST -H "Content-Type: application/json" -d '{"id":"test", "name":"testname"}' 127.0.0.1:8080/RestTestApp/Rest/Test/postjson  

standalone.shを実行している端末（ターミナルの画面）に、postしたjsonの"id"と"name"が出力されているはずです
