1)FILE CONFIG Kernel và Rootfs của BBB

2)BBB yocto dùng systemd hay system v


# 1) FILE CONFIG
Meta chứa các config của package và bbb

``````
yocto/poky-kirkstone/meta-yocto-bsp
``````
Trong machine sẽ chưa config của bbb

``````
cd ~/yocto/poky-kirkstone/meta-yocto-bsp/conf/machine/

ls
``````

Mở file config của bbb
``````
code beaglebone-yocto.conf 
``````

Trong đây sẽ có các config của Rootfs , Kernel 

Ví dụ như các device tree được dùng sẽ ở biến sau 

``````
KERNEL_DEVICETREE = "am335x-bone.dtb am335x-boneblack.dtb am335x-bonegreen.dtb"
``````
Version Kernel là 5.15
``````
PREFERRED_VERSION_linux-yocto ?= "5.15%"
``````
# 2) Kiểm tra systemd 
BBB yocto hỗ trợ sysvinit tuy nhiên ban đầu nó sẽ chưa được config vào

Để hỗ trợ sysvinit chúng ta cần thêm đoạn mã sau trong local.conf
``````
INIT_MANAGER = "sysvinit"
``````

Nếu muốn chuyển thành systemd thì chỉ cần như sau , tuy nhiên quá trình này sẽ mất thời gian do systemd ban đầu chưa được support

``````
INIT_MANAGER = "systemd"
``````
Ngoài systemd và sysvinit thì còn một cái nữa là BusyBox với BusyBox mdev 

Nó phù hợp dành cho các cấc file nhỏ 
``````
INIT_MANAGER = "mdev-busybox"
``````

Bạn có thể xem source code của manager init để biết cách nó cấu hình 

``````
cat meta/conf/distro/include/init-manager-systemd.inc
``````
Để enable feature tính thời gian trong yocto ta cần biết rằng nó là một tính năng của kernel vì thế câu lệnh để bật nó như sau
``````
APPEND += "printk.time=y"
``````
