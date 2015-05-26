#注释 *必须* 以分号结尾;
#请注意建表顺序;

#创建用户表;
create table  `userinfo`(
	`id` INT not null auto_increment,
	`username` VARCHAR(255) unique,
	`password` VARCHAR(255),
	`fullname` VARCHAR(255),
	`department` VARCHAR(255),
	`title` VARCHAR(255),
	primary key (`id`));

#创建工具表;
create table `tooling`(
	`id` INT not null auto_increment,
	`name` VARCHAR(255) unique,
	`amount` INT not null,
	primary key (`id`));

#创建订单表;
create table `orderform`(
	`id` INT not null auto_increment,
	`title` VARCHAR(255),
	`amount` INT not null,
	`price` DOUBLE not null,
	`date` DATETIME,
	`tooling_id` INT,
	`userinfo_id` INT,
	primary key (`id`));

alter table `orderform`  
	add index `FK_d4ov430b62mkvh944v6210h2h`(`userinfo_id`), 
	add constraint `FK_d4ov430b62mkvh944v6210h2h` 
	foreign key (`userinfo_id`) 
	references `userinfo`(`id`);
alter table `orderform`  
	add index `FK_omm3he0p8gfxqbn34a5d27blj`(`tooling_id`), 
	add constraint `FK_omm3he0p8gfxqbn34a5d27blj` 
	foreign key (`tooling_id`) 
	references `tooling`(`id`);

#创建工艺文件表;
create table `pdoc`(
	`id` INT not null auto_increment,
	`title` VARCHAR(255) unique,
	`content` LONGTEXT,
	`userinfo_id` INT,
	primary key (`id`));

alter table `pdoc`  
	add index `FK_kf77tk64lvmve4etby12icdhb`(`userinfo_id`), 
	add constraint `FK_kf77tk64lvmve4etby12icdhb` 
	foreign key (`userinfo_id`) 
	references `userinfo`(`id`);

#创建生产计划表;
create table `productionplan`(
	`id` INT not null auto_increment,
	`title` VARCHAR(255),
	`enddate` DATETIME,
	`isOnPlan` BIT not null,
	`isOnProducing` BIT not null,
	`pdoc_id` INT,
	`userinfo_id` INT,
	primary key (`id`));

alter table `productionplan`  
	add index `FK_ismm3fsl8kwlge2lo1pao1967`(`userinfo_id`), 
	add constraint `FK_ismm3fsl8kwlge2lo1pao1967` 
	foreign key (`userinfo_id`) 
	references `userinfo`(`id`);
alter table `productionplan`  
	add index `FK_rv4krkew51moohu9sh0gj9pbw`(`pdoc_id`), 
	add constraint `FK_rv4krkew51moohu9sh0gj9pbw` 
	foreign key (`pdoc_id`) 
	references `pdoc`(`id`);

#创建BOM表;
create table `bom`(
	`id` INT not null auto_increment,
	`amount` INT not null,
	`pdoc_id` INT,
	`tooling_id` INT,
	primary key (`id`));

alter table `bom`  
	add index `FK_h0x6234we9hgaialfl4ostpf5`(`pdoc_id`), 
	add constraint `FK_h0x6234we9hgaialfl4ostpf5` 
	foreign key (`pdoc_id`) 
	references `pdoc`(`id`);
alter table `bom`  
	add index `FK_tl85yroudlrbwb0d0i7ckmd3a`(`tooling_id`), 
	add constraint `FK_tl85yroudlrbwb0d0i7ckmd3a` 
	foreign key (`tooling_id`) 
	references `tooling`(`id`);

#创建表结束;

