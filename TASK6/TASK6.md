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
BBB yocto hỗ trợ systemd tuy nhiên ban đầu nó sẽ chưa được config vào

Để hỗ trợ systemd-boot chúng ta cần thêm đoạn mã sau
``````
EFI_PROVIDER="systemd-boot"
``````

