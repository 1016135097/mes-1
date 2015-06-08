#注释必须以分号结尾;
#请注意使用的数据库;
#必须是在数据库表完全新建的情况下使用，因为表之间对于的id是从0开始计数的
#请注意插入数据的顺序;

use pelloz;

#插入用户表数据;
insert into userinfo (id,username,password,fullname,department,title)
values 
( null,'admin','admin','管理员','admin','admin'),
( null,'process','process','张一','工艺室','工艺工程师'),
( null,'produce','produce','张二','车间生产','生产管理员'),
( null,'fixture','fixture','张三','工装室','工装工程师'),
( null,'plan','plan','张四','计划室','计划员'),
( null,'buyer','buyer','张五','采购部','采购工程师');

#插入工具表数据;
insert into tooling (id,name,amount,needPurchase)
values 
( null,'30度-硬质合金车刀',20,0),
( null,'45度-硬质合金车刀',20,0),
( null,'60度-硬质合金车刀',20,1),
( null,'90度-硬质合金车刀',20,0),
( null,'车刀杆',20,0),
( null,'d5-钻头',20,0),
( null,'d8-钻头',20,1),
( null,'d10-钻头',20,0),
( null,'d12-钻头',20,0),
( null,'d15-钻头',20,0),
( null,'倒角刀',20,1),
( null,'6面铣刀片',20,0),
( null,'8面铣刀片',20,0);


#插入订单表数据;
insert into orderform (id,title,amount,price,date,tooling_id,userinfo_id,isComplete)
values 
( null,'购买，30度-硬质合金车刀，20件',20,5.00,'2015-7-08',1,6,0),
( null,'购买，车刀杆，20件',20,150.00,'2015-7-08',5,6,0),
( null,'购买，d8-钻头，20件',20,50.00,'2015-7-08',7,6,0),
( null,'购买，倒角刀，20件',20,50.00,'2015-7-08',11,6,0);


#插入工艺文件表数据;
insert into pdoc (id,title,content,userinfo_id)
values 
( null,'ERT型-T01-前传动轴','车-钻-倒角(示例工艺简化处理)',2),
( null,'ERT型-T02-中传动轴','车-钻-倒角(示例工艺简化处理)',2),
( null,'ERT型-T03-后传动轴','车-钻-铣-倒角(示例工艺简化处理)',2);


#插入生产计划表数据;
insert into plan (id,title,num,enddate,isOnPlan,isOnProducting,isComplete,pdoc_id,userinfo_id)
values 
( null,'ERT型-T01-前传动轴-生产200件',200,'2015-6-25',1,1,0,1,5),
( null,'ERT型-T02-中传动轴-生产300件',300,'2015-6-15',1,0,0,1,5),
( null,'ERT型-T02-中传动轴-生产400件',400,'2015-6-19',1,0,0,2,5),
( null,'ERT型-T03-后传动轴-生产100件',100,'2015-6-24',0,0,0,2,5),
( null,'ERT型-T03-后传动轴-生产150件',150,'2015-6-30',0,0,0,3,5),
( null,'ERT型-T03-后传动轴-生产562件',562,'2015-6-17',0,0,0,2,5);



#插入BOM表数据;
insert into bom (id,amount,pdoc_id,tooling_id)
values 
( null,2,1,1),
( null,2,1,2),
( null,2,1,5),
( null,2,1,6),
( null,2,1,11),
( null,3,2,1),
( null,3,2,3),
( null,3,2,5),
( null,3,2,7),
( null,3,2,11),
( null,4,3,1),
( null,4,3,3),
( null,4,3,5),
( null,4,3,7),
( null,4,3,11),
( null,4,3,12);



#插入表数据结束;