#注释 *必须* 以分号结尾;
# 删除表的时候注意顺序;
use pelloz;

DROP TABLE IF EXISTS `plan`;
DROP TABLE IF EXISTS `orderform`;
DROP TABLE IF EXISTS `bom`;
DROP TABLE IF EXISTS `pdoc`;
DROP TABLE IF EXISTS `tooling`;
DROP TABLE IF EXISTS `userinfo`;

# 表的结构 `orderform`;

CREATE TABLE IF NOT EXISTS `orderform` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `amount` int(11) NOT NULL,
  `price` double NOT NULL,
  `date` date DEFAULT NULL,
  `isComplete` bit(1) NOT NULL,
  `tooling_id` int(11) DEFAULT NULL,
  `userinfo_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_omm3he0p8gfxqbn34a5d27blj` (`tooling_id`),
  KEY `FK_d4ov430b62mkvh944v6210h2h` (`userinfo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


# 表的结构 `plan`;

CREATE TABLE IF NOT EXISTS `plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `num` int(11) NOT NULL,
  `endDate` date DEFAULT NULL,
  `isOnPlan` bit(1) NOT NULL,
  `isOnProducting` bit(1) NOT NULL,
  `isComplete` bit(1) NOT NULL,
  `pdoc_id` int(11) DEFAULT NULL,
  `userinfo_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_mlajc44o8r0mb31so0tl3tljw` (`pdoc_id`),
  KEY `FK_oq2lolvyl15qe4pfeerq6au9u` (`userinfo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


# 表的结构 `pdoc`;

CREATE TABLE IF NOT EXISTS `pdoc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `content` longtext,
  `userinfo_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hxvs0cdnutcksojp1el35a9k1` (`title`),
  KEY `FK_kf77tk64lvmve4etby12icdhb` (`userinfo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


# 表的结构 `bom`;

CREATE TABLE IF NOT EXISTS `bom` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` int(11) NOT NULL,
  `pdoc_id` int(11) DEFAULT NULL,
  `tooling_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_h0x6234we9hgaialfl4ostpf5` (`pdoc_id`),
  KEY `FK_tl85yroudlrbwb0d0i7ckmd3a` (`tooling_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


# 表的结构 `tooling`;

CREATE TABLE IF NOT EXISTS `tooling` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `amount` int(11) NOT NULL,
  `needPurchase` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r6reckbln8m1k8fddxvwvkoeh` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


# 表的结构 `userinfo`;

CREATE TABLE IF NOT EXISTS `userinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_45fvrme4q2wy85b1vbf55hm6s` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


# 限制表 `bom`;

ALTER TABLE `bom`
  ADD CONSTRAINT `FK_tl85yroudlrbwb0d0i7ckmd3a` FOREIGN KEY (`tooling_id`) REFERENCES `tooling` (`id`),
  ADD CONSTRAINT `FK_h0x6234we9hgaialfl4ostpf5` FOREIGN KEY (`pdoc_id`) REFERENCES `pdoc` (`id`);


# 限制表 `orderform`;

ALTER TABLE `orderform`
  ADD CONSTRAINT `FK_d4ov430b62mkvh944v6210h2h` FOREIGN KEY (`userinfo_id`) REFERENCES `userinfo` (`id`),
  ADD CONSTRAINT `FK_omm3he0p8gfxqbn34a5d27blj` FOREIGN KEY (`tooling_id`) REFERENCES `tooling` (`id`);


# 限制表 `pdoc`;

ALTER TABLE `pdoc`
  ADD CONSTRAINT `FK_kf77tk64lvmve4etby12icdhb` FOREIGN KEY (`userinfo_id`) REFERENCES `userinfo` (`id`);


# 限制表 `plan`;

ALTER TABLE `plan`
  ADD CONSTRAINT `FK_oq2lolvyl15qe4pfeerq6au9u` FOREIGN KEY (`userinfo_id`) REFERENCES `userinfo` (`id`),
  ADD CONSTRAINT `FK_mlajc44o8r0mb31so0tl3tljw` FOREIGN KEY (`pdoc_id`) REFERENCES `pdoc` (`id`);
