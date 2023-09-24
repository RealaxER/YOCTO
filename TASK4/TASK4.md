1) Package dependencies and splitting
2) What is DISTRO , MACHINE , local.config

# 1)What is Package dependencies and splitting
* Là một tool thuộc phần openembedded,giúp ta tách src thành các package thuận tiên cho việc install hay remove khỏi dự án một cách dễ dàng
---
* Tách package trong các phiên bản mới không cho phép tự do đặt tên nên ta cần tuân thủ theo các tên sau.


FILES_${PN} : Nơi chứa các file hay thư mục hay các thứ cần thiết cho hệ thống.

```
FILES_${PN} = "\
    ${bindir}/* \
    ${sbindir}/* \
    ${libexecdir}/* \
    ${libdir}/lib*.so.* \
    ${sysconfdir} \
    ${sharedstatedir} \
    ${localstatedir} \
    /bin/* \
    /sbin/* \
    /lib/*.so* \
    ${datadir}/${PN} \
    ${libdir}/${PN}/* \
    ${datadir}/pixmaps \
    ${datadir}/applications \
    ${datadir}/idl \
    ${datadir}/omf \
    ${datadir}/sounds \
    ${libdir}/bonobo/servers"
```
FILES_${PN}-dbg : chứa các thông tin debug,gỡ lỗi 
```
FILES_${PN}-dbg = "\
    ${bindir}/.debug \
    ${sbindir}/.debug \
    ${libexecdir}/.debug \
    ${libdir}/.debug \
    /bin/.debug \
    /sbin/.debug \
    /lib/.debug \
    ${libdir}/${PN}/.debug"
```
FILES_${PN}-doc: tài liệu, được tách riêng ra để tránh không cần install 
```
FILES_${PN}-doc = "\
    ${docdir} \
    ${mandir} \
    ${infodir} \
    ${datadir}/gtk-doc \
    ${datadir}/gnome/help"
```
FILES_${PN}-dev: chứa các src và thư viện và tool cho developer.
```
FILES_${PN}-dev = "\
    ${includedir} \
    ${libdir}/lib*.so \
    ${libdir}/*.la \
    ${libdir}/*.a \
    ${libdir}/*.o \
    ${libdir}/pkgconfig \
    /lib/*.a \
    /lib/*.o \
    ${datadir}/aclocal"
```
FILES_${PN}-locale : các tệp tin khác 
```
FILES_${PN}-locale = "${datadir}/locale"
```
----
Ta thực hiện ví dụ sau giả sử bạn có một helloworld.c và một hello.bb
Thêm đoạn mã sau vào hello.bb
```
PACKAGES = "${PN} ${PN}-dbg ${PN}-dev"

FILES_${PN} = "/usr/bin/helloworld"

FILES_${PN}-dbg += "${bindir}/.debug"

FILES_${PN}-dev += "${bindir}/.lib/*.so"

```
Giải thích qua chút đầu tiên ta tách các package của helloworld ra sau đó thêm nó vào biến PACKAGES để build, các thư mục kia sẽ được tự động tạo

Gọi nó trong core-image-minimal.bb
```
IMAGE_INSTALL:append = " hello"
IMAGE_INSTALL:append = " hello-dbg"
IMAGE_INSTALL += "hello-dev"
```
Build image
```
bitbake core-image-minimal 
```

Check thử package sau khi được tạo ra 
```
tree ~/yocto/bbb/build/tmp/work/cortexa8hf-neon-poky-linux-gnueabi/hello/1.0-r0/packages-split
```


# 2)  What is DISTRO , MACHINE , local.config
* DISTRO: Biến này xác định bản phân phối (distribution) bạn đang sử dụng trong dự án Yocto. Nó chứa tên bản phân phối và mô tả cấu hình, chẳng hạn như phiên bản trình điều khiển giao diện máy (init system), hệ thống quản lý gói (package manager), và các cài đặt khác. Điều này cho phép bạn tùy chỉnh dự án Yocto cho bất kỳ bản phân phối nào bạn muốn tạo. Ví dụ: DISTRO = "poky".

* MACHINE: Biến này xác định máy chủ mục tiêu (target machine) cho việc xây dựng. Máy chủ mục tiêu là hệ thống hoặc bo mạch mà bạn muốn tạo bản phân phối cho. Máy chủ mục tiêu định nghĩa các yêu cầu cấu hình phần cứng và kiến thức về cách xây dựng hệ thống cho máy chủ mục tiêu cụ thể đó. Một số ví dụ về MACHINE bao gồm qemuarm, raspberrypi3, beaglebone,...

* local.conf: Tệp local.conf (hoặc conf/local.conf) là một tệp cấu hình cấp dự án Yocto. Đây là nơi bạn có thể tùy chỉnh các biến cài đặt, như biến MACHINE, DISTRO, và nhiều biến khác để điều chỉnh quá trình xây dựng. Tệp local.conf cho phép bạn thiết lập các giá trị mặc định và thay đổi chúng dựa trên nhu cầu cụ thể của dự án. Nó thường được đặt trong thư mục conf của thư mục cấu hình Yocto của bạn.

#### Ví dụ về một số cấu hình trong tệp local.conf:

* MACHINE: Xác định máy chủ mục tiêu.
* DISTRO: Xác định bản phân phối.
* DL_DIR: Xác định thư mục tải về (download directory) cho các nguồn tải về.
* SSTATE_DIR: Xác định thư mục lưu trạng thái sstate.
* TMPDIR: Xác định thư mục làm việc tạm thời.
* BB_NUMBER_THREADS và * PARALLEL_MAKE: Điều chỉnh đồng thời các luồng xây dựng.





