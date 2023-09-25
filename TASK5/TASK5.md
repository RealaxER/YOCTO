1) CROSS_COMPILE FOR APPLICATION 
2) Find recipes file uboot ,kernel ,rootfs
3) Add new kernel module 
# 1) How to use CROSS_COMPILE  

### Ví dụ cách dùng SDK 
```
cd mytest/sdk
source environment-setup-cortexa8hf-neon-poky-linux-gnueabi
```
### Check thử GCC của SDK 
```
$CC -v 
```
Thử biên dịch với hello.c
```
$CC -o hello hello.c
```
Tuy nhiên đây là cross_compiler nên hello phải chạy trên bbb hoặc là queme 

```
cp hello path/debian
```
Boot bbb và 
```
./hello
```


# 2) Find recipes file uboot ,kernel ,rootfs
* Xem các recipes mà bsp 

* U-BOOT  
```
cd yocto/poky-kirkstone/meta/
ls
```
Check cây thư mục 
```
tree recipes-bsp/u-boot
```
File chúng ta sẽ chỉnh sửa là u-boot_2022.01.bb

* KERNEL

Check xem các recipes cần sửa trong linux 

```
ls recipes-kernel/linux
```

```
cat recipes-kernel/linux/linux-yocto-dev.bb
```

* ROOTFS
Là hệ thống các tệp tin file sử dụng được thêm vào trong core-image

```
ls recipes-core
```

```
cat recipes-core/core-image-minimal.bb
```
# 3) Build Kernel Module 
```
cd meta-mylayer
```
Tạo folder recipes mới 
```
mkdir -p recipes-module/hello/files
```
Tạo các file cần thiết
```
touch recipes-module/hello/files/hello.c
touch recipes-module/hello/files/Makefile
touch recipes-module/hello/hello.bb
```

hello.bb
```
SUMMARY = "Example of how to build an external Linux kernel module"
DESCRIPTION = "${SUMMARY}"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit module

SRC_URI = "file://Makefile \
           file://hello.c \
          "

S = "${WORKDIR}"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.

RPROVIDES:${PN} += "kernel-module-hello"
```
Makefile 
```
obj-m := hello.o

SRC := $(shell pwd)

all:
	$(MAKE) -C $(KERNEL_SRC) M=$(SRC)

modules_install:
	$(MAKE) -C $(KERNEL_SRC) M=$(SRC) modules_install

clean:
	rm -f *.o *~ core .depend .*.cmd *.ko *.mod.c
	rm -f Module.markers Module.symvers modules.order
	rm -rf .tmp_versions Modules.symvers
```
hello.c
```
#include <linux/module.h>

static int __init hello_init(void)
{
	pr_info("Hello World!\n");
	return 0;
}

static void __exit hello_exit(void)
{
	pr_info("Goodbye Cruel World!\n");
}

module_init(hello_init);
module_exit(hello_exit);
MODULE_LICENSE("GPL");
```
Build image
```
bitbake core-image-minimal
```
Nếu bạn find sẽ thấy không có hello.ko vì nó chưa được thêm vào image

Trong yocto có 4 cách thêm kernel module vào image

* MACHINE_ESSENTIAL_EXTRA_RDEPENDS
* MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS
* MACHINE_EXTRA_RDEPENDS
* MACHINE_EXTRA_RRECOMMENDS

Theo khuyến nghị thì nên sử dụng MACHINE_EXTRA_RRECOMMENDS

Thêm vào core-image-minimal.bb hoặc là local.config 
```
MACHINE_EXTRA_RRECOMMENDS = "kernel-module-hello"
```

Check thử kernel module hello 

```
find -name hello.ko
```
Nạp lại image và thử 

Tuy nhiên kernel module sẽ nằm rải rác trong các thư mục cấu hình sẽ rất khó tìm vì thế để nhanh ta sẽ dùng modprobe

```
modprobe hello
dmesg
```

Nếu muốn nó tải auto insmod thì cần thêm lệnh autoload sau 
```
KERNEL_MODULE_AUTOLOAD += "hello"
```
---
Cách trên tạo file.ko giúp bạn có thể insmod dễ dàng giờ ta hãy thử thêm nó vào cây kernel bằng devtool

---
Check thử các source linux 
```
bitbake -s | grep linux 
```
Chúng ta sẽ lấy linux-yocto source làm source cây , bạn cũng có thể thêm riêng source của bản thân
```
devtool modify linux-yocto
```
devtool sẽ dùng tự tạo 1 layer mới nhất để chỉnh sửa source và thêm vào layer đó 

Trước mình có 1 meta tạo bằng devtool có tên là meta-test vì thế giờ nó tạo ra 1 layer meta-test và lưu src kernel 

```
ls meta-test/sources/linux-yocto
tree -L 2 meta-test/
```

Move hello kernel folder của mình sang đây và sửa lại 
```
mv meta-mylayer/recipes-module/hello/ meta-test/sources/linux-yocto/drivers/
```

```
tree meta-test/sources/linux-yocto/drivers/hello/
```

Tuy nhiên file kernel thì chỉ cần Kconfig Makefile và source.c thôi 

Chúng ta sẽ giữ lại hello.c Makefile và xóa các file còn lại đi , các file sẽ cùng nằm trong 1 folder hello

hello/

├── hello.c

├── Kconfig

└── Makefile


Kconfig
```
config HELLO
        tristate 'Create a my hello module'
        default y
        help
                This is a module to print Hello World!

```

Makefile 
```
obj-$(CONFIG_HELLO) += hello.o
```

Thêm vào Kconfig và Makefile của drivers


meta-test/sources/linux-yocto/drivers/Kconfig
```
source "drivers/char/hello/Kconfig"
```

meta-test/sources/linux-yocto/drivers/Makefile 
```
obj-$(CONFIG_HELLO) += hello/
```
Chạy thử menuconfig bằng máy ảo 
```
bitbake virtual/kernel -c menuconfig
```

Build kernel 
```
devtool build linux-yocto
```
Build lại image và nạp
```
devtool build-image core-image-minimal
```
Check trên board bbb 
```
dmesg | grep Hello 
```

