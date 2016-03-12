# PaaS-BE-RM-Agent
工程说明
代理工程主要分为四个工程：
1、	agent-common
a)	用于以下三个工程通用组件
2、	agent-client
a)	代理客户端工程，用于连接服务器，处理接收服务端指令，并将结果反馈给服务端。
3、	agent-server
a)	代理服务端工程，用于向指定客户端发送指令。
4、	agent-web
a)	用于指收外围系统指令的web工程，本工程包含server工程。
server端配置
Agent Server端主要部署在公司内部网络。

agentConfig.property
修改Agent web/agentConfig.property文件

目前此文件只有以下两个属性生效。
#server端地址
server.ip=10.1.241.127
#服务端监控端口
server.port=8888

server.ip主要是部署服务器的主机地址。
server.port主要是监控端口。

client端配置
Agent Client主要部署在公网上的各云平台上。

agentClientConfig.property
修改/agentClientConfig.property文件
#客户端标识
agent.Client.info={aid:"A",name:"hello",common:"备注"}

agent.Client.info主要是当前客户端的标识。

agentConfig.property
修改Agent web/agentConfig.property文件

目前此文件只有以下两个属性生效。
#server端地址
server.ip=10.1.241.127
#服务端监控端口
server.port=8888

server.ip主要是部署服务器的主机地址。
server.port主要是监控端口。

测试环境信息
Agent web api地址：
http://10.1.241.127:8880/agent-web-api

开放两个接口。
http://10.1.241.127:8880/agent-web-api/simpCommand/exec 
post json ：
{"aid":"dev","command":"chmod u+x /root/test.sh"}

http://10.1.241.127:8880/agent-web-api/simpFile/upload 
post json:
{"aid":"dev","content":"echo '123210000'","fileName":"test.sh","path":"/root"}
服务部署信息

agent web api服务器信息：
10.1.241.127
目录：/aifs01/apache-tomcat-8.0.26

部署：
先将工程打包，maven package
然后到服务器上停服务，并将工程删除。
再将打好的agent web api包上传到服务器，再重启tomcat


agent client服务器信息：
10.1.241.122
目录：/root/agent.client

部署：
将agent client 打包，生成agent client.jar包。
到服务器上停止客户端服务。（kill -9 pid）
上传打好的jar包。（名称与服务器一致）
执行启动脚本。（./start_client.sh）
