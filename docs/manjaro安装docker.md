# 安装docker

+ 安装
    sudo pacman -S docker
  
+ 启动docker服务:
    sudo systemctl start docker 

+ 查看docker服务的状态:
    sudo systemctl status docker
    
+ 设置docker开机启动服务:
    systemctl enable docker 
    
+ 镜像加速器
    sudo vim /etc/docker/daemon.json
    加上
    {
    	"registry-mirrors": ["https://docker.mirrors.ustc.edu.cn"]
    }
    
# 操作命令
    
+ 查看 image 列表
    docker iamges
    
+ 下载镜像
    docker pull 镜像名
    
+ 删除镜像
    - 删除 image 之前，需要先删除容器 container:
      docker ps -a
      docker rm container_id/container_name
      
    -删除 image:
      docker rmi <image-id>
      docker rmi <image-name>:<tag>

+ 容器操作  cid为容器名
    - 查看运行中的容器  docker ps
    - 查看所有容器 docker ps -a
    - 显示运行的容器里的进程信息 docker top cid
    - 显示容器详细信息 docker inspect cid
    - 日志查看 docker logs cid
    - 实时查看日志输出  docker logs -f cid
    - 查看容器root用户密码 docker logs cid 2>&1 | grep '^User: ' | tail -n1

+ 容器运行
    语法格式：
    
    Usage:	docker run [OPTIONS] IMAGE [COMMAND] [ARG...]
    例如：
    
    docker run -it --name cidregistry.domain.com/library/ubuntu:14.04
    如果直接 docker run -it registry.domain.com/library/ubuntu 可能会出错，因为不加 tag ，默认就去运行 latest 版本，而本地没有 latest 版本，所以，需要将 image+tag，以冒号分隔拉去。
    
    -i 交互操作
    -t 终端
    --name 指定容器名字
    -d 后台运行一个容器
    --rm，表明退出容器后随之将其删除，可以避免浪费空间
    -p 映射端口
    -v 挂载 volumn
    --privileged=false 容器内 root 拥有真正 root 权限
    当处于一个容器中时，利用exit退出容器
    
+ 启动已终止（stop）容器：（start、stop、restart）
    docker restart 3e8 # 3e8 为容器的 id 号，不需要全写，也可以用容器名替代
    
+ 进入容器
    - 进入正在运行的容器，退出不会造成容器停止：
    docker exec -it cid /bin/bash
    
    - 附着到正在运行的容器中，退出时会导致容器终止，不常用：
    docker attach cid

+ 拷贝文件出来
    docker cp cid:/container_path to_path
+ 删除容器
    docker rm cid
+ 强制删除
    docker rm -f cid
+ 删除所有容器
    -q 表示只列出容器的 id 值
    docker rm `docker ps -a -q`
    
+ 容器运行状态修改
    docker start/stop/kill/restart cid
    
+ 更改容器名字
    docker rename old new
    
## docker安装mysql
    
        sudo docker run -d \
        > --name mysql \
        > -p 3306:3306 \
        > -e MYSQL_ROOT_PASSWORD=123456 \
        > -d mysql 
    
    sudo docker exec -it mysql bash
    
    mysql -u root -p
    Enter password: 
    ERROR 2002 (HY000): Can't connect to local MySQL server through socket '/var/run/mysqld/mysqld.sock' (2)
    
    mysql -h 127.0.0.1 -u root -p
    
    create user 'miao'@'%' identified with mysql_native_password by '123456';
    grant all privileges on *.* to 'miao'@'%';