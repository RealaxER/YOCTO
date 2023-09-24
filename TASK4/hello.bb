SUMMARY = "Simple helloworld application"
# Mô tả về file recipe
SECTION = "examples"
# Phân loại recipe , không có cũng được , 
LICENSE = "MIT"
# Giấy phép 
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
# Giấy phép kiểm tra xem có thay đổi hay không
# Cú pháp chung 

# Nơi lấy dữ liệu
SRC_URI = "file://helloworld.c"


# Biến toàn cục của bitbake 
S = "${WORKDIR}"
# Sử dụng để xác định vị trí thư mục làm việc 
# Sau đó bitbake sẽ gọi nó


# Biên dịch helloworld 
do_compile() {
    ${CC} ${LDFLAGS} helloworld.c -o helloworld
}

# install 
do_install() {
    install -d ${D}${bindir}
    install -m 0755 helloworld ${D}${bindir}
}



PACKAGES = "${PN} ${PN}-dbg ${PN}-dev"
FILES_${PN} = "\
    /usr/bin/helloworld"

FILES_${PN}-dbg += "${bindir}/.debug"


FILES_${PN}-dev += "${bindir}/.lib/*.so"

