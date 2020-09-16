#Manjaro操作

+. 更新系统
    sudo pacman -Syyu
    - 注释：
       S 是同步包
       y 是刷新
       u 是升级
    必须要做timeshift，以防你哪天玩坏只能重装
    
+ 清理未安装的包文件
    sudo pacman -Sc
    
+ 删除单个软件包,保留其全部已经安装的依赖关系
    sudo pacman -R package_name
    
+ 删除指定软件包,及其所有没有被其他已安装软件包使用的依赖关系
    sudo pacman -Rs package_name
    
+ 要删除指定软件包和所有依赖这个软件包的程序:
    sudo pacman -Rsc package_name
    注意: 此操作是递归的,请小心检查,可能会一次删除大量的软件包.   