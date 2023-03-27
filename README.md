# JiP
Java interacts with Python

## 主要功能
1. SpringBoot与Flask相互发送请求并响应；
2. SpringBoot与Flask使用Sftp传输文件。

### springDemo
- 发送请求[x]
通过WebClient实现
- 接收请求[x]
- 调用sftp读写远程文件[x]
通过jcraft实现

### flaskDemo
- 发送请求[x]
通过requestsuu实现
- 接收请求[x]
- 调用sftp读写远程文件[x]
通过paramiko实现

## 使用方法
### 克隆仓库
```
git clone https://github.com/Zoybzo/JiP.git
```
### 添加配置文件
#### flaskdemo
```
cd flaskdemo/config
touch sftpConfig.ini
```
文件格式如
```
[sftp]
hostname =
port = 22
springboot-port=
username =
password =
timeout = 6000
remotepath =
# remotepath取决于另一个服务器的sftp配置中的ChrootDir
localpath =

[server]
hostname =
port =
```
#### springboot
config.properties
```
cd springDemo/src/main/resources
touch config.properties
```
文件格式如
```
sftp.ip=
sftp.port=22
sftp.username=
sftp.password=
sftp.timeout=6000
sftp.remotepath=
sftp.localpath=
```
application.yml
文件格式如
```
server:
  port:
webclient:
  hostname: "http://domain:port"
  port: port
```

### 服务器端配置sftp 以ubuntu为例
参考：https://linuxhint.com/setup-sftp-server-ubuntu/
```shell
sudo apt install ssh
sudo vim /etc/ssh/sshd_config
```
在sshd_config中添加
```
# Match gourp 匹配用户组
Match group sftp
# ChrootDirectory sftp连接后的根目录
ChrootDirectory /home
# X11Forwarding X11 forwarding is a mechanism that allows a user to start up remote applications but forward the application display to your local machine using tunneling over an SSH session.
X11Forwarding no
AllowTcpForwarding no
ForceCommand internal-sftp
```
The above configuration will allow the sftp users group to access their home directories through the SFTP. However, not allowed to access the normal SSH shell. Save the above-mentioned lines in the configuration file and close it.
```shell
sudo systemctl restart ssh
sudo addgroup sftp
sudo useradd -m sftpuser -g sftp
sudo passwd sftpuser
```
```shell
sudo chmod 755 /home/sftpuser/
```

### 服务器部署

#### springboot 打包成Jar包并部署到服务器
运行
```
nohup java -jar springDemo-0.0.1-SNAPSHOT.jar > demo.log>&1&
```

#### flask 部署到服务器
```
nohup python app.py &
```

## 调用过程
访问springdemo的/sftp/sftp接口后，触发文件上传任务，通过sftp将文件上传至flask服务器，并在完成后调用flask服务器的/sftp接口，flask服务器将自己的文件上传至springboot服务器，并在完成后调用springboot的/sftp/test接口。
