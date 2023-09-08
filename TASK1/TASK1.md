1.Yocto terminology 

.conf : là các file cấu hình của 1 thiết bị , được sử dụng để cấu hình build ví dụ như là sau khi build lưu ở đâu , build thêm những recipe nào, có nhiều file .conf khác nhau tên file dùng để chỉ định nhiệm vụ cụ thể của nó như bblayer.conf để xác định layer nào cần build


.bb : nó giống như 1 file công thức nấu ăn , bên trong nó ghi các nguyên liệu cần nấu , đường dẫn của nó , cách nấu như thế nào , nấu bằng makefile hay cmake.


.bbappend : các file .bb đôi khi có thể quá dài để sửa đôi , thì mình có thể dùng .bbappend để thêm công thức nấu ăn ,nối thêm vào file cũ.

.bbclass : định nghĩa các cài đặt và biến cụ thể mà các recipes có thể kế thừa và chia sẻ. Khi một recipes kế thừa từ một .bbclass, nó có thể sử dụng các biến và cài đặt đã được định nghĩa trong .bbclass đó.


.inc :  chứa các đoạn mã hoặc cài đặt được sử dụng trong recipes để tái sử dụng. Các file này thường chứa các biến mô tả hoặc thiết lập cho một gói phần mềm cụ thể hoặc một nhóm gói phần mềm liên quan.



a) What is recipe : Là các file .bb và .bbappend 

b) What is configuration file : Là các file cấu hình .conf mỗi file sẽ có chức năng cấu hình cụ thể như layer

c)What is class : là các file .bbclass như autotools.bbclass được dùng cho Autotools

d) What is layer : là một kho hoặc là một thư mục chứa tập hợp tất cả các recipes và config file để báo cho Yocto biết phải làm gì , tách biệt các metadata với các config file

e) What is metadata : là tập hợp thông tin liên quan đến recipes, bbclass  file config và hướng dẫn xây dựng cũng như dữ liệu được sử dụng.

f) What is package : là tập hợp các têp source và file .bb để biên dịch 


How to find our bitbake file ?

+ Các file bitbake out sẽ được tạo ra ở trong thư mục tmp/work/board-name/

+ Built it : bitbake -c populate_sdk core-image-minimal
1.2 Create a simple layer 

SDK là một tập hợp các công cụ và tài liệu để có thể phát triển ứng dụng, thư viện, và các thành phần phần mềm khác cho một nền tảng hoặc hệ thống cụ thể.

Sử dụng các công cụ chuyển mã nguồn từ máy tính phát triển sang máy tính hoặc thiết bị nhúng mà không cần tải lại toàn bộ Yocto Project.


*Create your own yocot layer 

bitbake-layers create-layer meta-mylayer 

bitbake-layers add-layer meta-mylayer 

bitbake-layers show-layers 

├── conf

│   └── layer.conf

├── COPYING.MIT

├── README

├── recipes-example

│   └── example

│       ├── example_0.1.bb

│       └── files

│           └── hello.c


bitbake core-image-minimal

