-- MySQL dump 10.13  Distrib 8.4.3, for macos14 (arm64)
--
-- Host: localhost    Database: blog_system
-- ------------------------------------------------------
-- Server version	8.4.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ad`
--

DROP TABLE IF EXISTS `ad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ad` (
  `ad_id` varchar(50) NOT NULL COMMENT '广告id',
  `ad_type_id` varchar(50) DEFAULT NULL COMMENT '广告类型',
  `ad_title` varchar(255) DEFAULT NULL COMMENT '广告标题',
  `ad_img_url` varchar(500) DEFAULT NULL COMMENT '广告图片的url地址',
  `ad_link_url` varchar(50) DEFAULT NULL COMMENT '广告链接地址',
  `ad_sort` int DEFAULT NULL COMMENT '广告排序（越小越靠前）',
  `ad_begin_time` datetime DEFAULT NULL COMMENT '广告开始时间',
  `ad_end_time` datetime DEFAULT NULL COMMENT '广告结束时间',
  `ad_add_time` datetime DEFAULT NULL COMMENT '添加广告时间',
  PRIMARY KEY (`ad_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ad`
--

LOCK TABLES `ad` WRITE;
/*!40000 ALTER TABLE `ad` DISABLE KEYS */;
INSERT INTO `ad` VALUES ('1b3902a4914245599ab49a0eb5281282','77048c96e2d34f7fb8f2dbbf7d2789f5','汽水音乐','https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/ad/0a63906c3f72484483d928a081131b9b.png','https://www.qishui.com/',2,'2025-05-31 23:30:23','2025-07-10 23:30:23','2025-05-31 23:30:23'),('33d2961a08224fcd8d0c9a2d17705216','77048c96e2d34f7fb8f2dbbf7d2789f5','番茄小说','https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/ad/d2e3aa570e8440368a974325b7e88b07.jpg','https://fanqienovel.com',2,'2025-06-25 00:00:00','2025-06-19 00:00:00','2025-06-21 22:58:21'),('7770d9bf206440f4bf393fc9e5e7c3c1','77048c96e2d34f7fb8f2dbbf7d2789f5','QQ音乐','https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/ad/9ba0d5cde26342958b6f3fb72ff594f8.webp','https://y.qq.com',2,'2025-07-09 00:00:00','2025-07-25 00:00:00','2025-07-15 08:52:35'),('b3cf8f286c834e0897d722469badb324','88326ff9b2454be88d8e7d90168190d7','大白兔','https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/ad/0cf36414ff22dd8283d59c0121321280.jpg','https://www.baidu.com/',1,'2025-06-10 23:30:23','2025-07-20 23:30:23','2025-06-10 23:30:23'),('d03e5066cf78480c83de3260f49e2aa8','88326ff9b2454be88d8e7d90168190d7','广告标题3','https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/ad/70347aeeea4c0cb3227c3f2191cb2378.jpg','https://www.baidu.com/',3,'2025-05-21 23:30:23','2025-07-20 23:30:23','2025-05-21 23:30:23'),('e15a02e387e14740b7e1dd7a456962bb','77048c96e2d34f7fb8f2dbbf7d2789f5','淘宝','https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/ad/28437d4f8c30412ab89ee09bcd3a3128.png','https://www.taobao.com',1,'2025-06-11 00:00:00','2025-06-26 00:00:00','2025-06-21 22:59:12');
/*!40000 ALTER TABLE `ad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ad_type`
--

DROP TABLE IF EXISTS `ad_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ad_type` (
  `ad_type_id` varchar(50) NOT NULL COMMENT '广告类型id',
  `ad_type_title` varchar(35) DEFAULT NULL COMMENT '广告类型名称',
  `ad_type_tag` varchar(35) DEFAULT NULL COMMENT '广告标识（首页顶部广告，轮廓图广告，文章详情广告）',
  `ad_type_sort` int DEFAULT NULL COMMENT '广告类型排序（越小越靠前）',
  `ad_type_add_time` datetime DEFAULT NULL COMMENT '广告类型添加时间',
  PRIMARY KEY (`ad_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ad_type`
--

LOCK TABLES `ad_type` WRITE;
/*!40000 ALTER TABLE `ad_type` DISABLE KEYS */;
INSERT INTO `ad_type` VALUES ('77048c96e2d34f7fb8f2dbbf7d2789f5','文章页面广告','ArticleAd',1,'2025-04-01 23:30:23'),('88326ff9b2454be88d8e7d90168190d7','首页轮播图广告','homeAd',2,'2025-03-12 23:30:23');
/*!40000 ALTER TABLE `ad_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `admin_id` varchar(50) NOT NULL COMMENT '管理员id',
  `admin_name` varchar(10) DEFAULT NULL COMMENT '管理员姓名',
  `admin_password` varchar(10) DEFAULT NULL COMMENT '管理员密码',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('100110','jack','200504'),('100111','tom','200505');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article` (
  `article_id` varchar(50) NOT NULL COMMENT '文章id',
  `article_type_id` varchar(50) DEFAULT NULL,
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户id',
  `article_title` varchar(500) DEFAULT NULL COMMENT '文章标题',
  `article_add_time` datetime DEFAULT NULL COMMENT '文章添加时间',
  `article_content` text COMMENT '文章内容',
  `article_good_number` int DEFAULT NULL COMMENT '点赞次数',
  `article_look_number` int DEFAULT NULL COMMENT '阅读次数',
  `article_collection_number` int DEFAULT NULL COMMENT '收藏次数',
  PRIMARY KEY (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES ('13','2','14user','仰望：人类永恒的星空之问','2025-06-10 14:11:13','\n\n当远古的智人第一次驻足凝望夜空，那些闪烁的光点便成了刻在基因里的永恒叩问。千万年过去，我们仍被同一片星空笼罩，却已能用科学之眼穿透亿万光年。天文，这门最古老的科学，始终承载着人类对存在本质的终极探索。\n\n**星辰：宇宙历史的活化石**\n猎户座腰带三星的光芒，实则是八百年前发出的古老信使；仙女座星系模糊的光斑，展现的是二百五十万年前的宇宙图景。望远镜不仅是空间探测器，更是时间机器——我们观测得越深远，便越接近宇宙的童年。超新星遗迹中检测到的重元素，揭示了我们身体里的钙与铁皆源自垂死恒星的剧烈爆发。正如卡尔·萨根所言：\"我们是星尘的自觉体。\"\n\n**尺度：颠覆认知的维度课堂**\n地球在太阳系中尚不如沙滩上的一粒细沙，而太阳系本身在银河系旋臂中不过是一个暗淡的光点。可观测宇宙中约两万亿个星系，每个星系又包含千亿颗恒星，这种尺度足以让任何人类中心的幻想瓦解。当旅行者1号在64亿公里外回望地球，拍下那粒\"暗淡蓝点\"时，人类第一次直观认识到：所有文明史都发生在这个悬浮在太阳光束中的微尘之上。\n\n**未知：暗物质的永恒谜题**\n可观测物质仅占宇宙总质能的5%，剩余95%由暗物质与暗能量构成——这堪称现代科学最大的\"认知黑洞\"。引力透镜现象证明暗物质确实存在，但至今无法直接探测；暗能量更推动着宇宙加速膨胀，却连理论模型都充满矛盾。正如爱因斯坦感叹：\"宇宙最不可理解之处，在于它居然可以被理解。\"每解开一个天文谜题，往往暴露出更深层的未知领域。\n\n在智利阿塔卡马沙漠，ALMA射电望远镜阵列正接收130亿年前的第一代恒星信号；在贵州群山之中，\"中国天眼\"FAST监听可能的外星文明电波。这些现代巴别塔延续着祖先的仰望姿态，只是追问已从\"星星是什么\"升级为\"我们在多重宇宙中是否唯一\"。\n\n或许天文的终极价值，不在于找到所有答案，而在于它永恒提醒着人类的渺小与伟大。渺小如我们，竟能测量星系旋转曲线，推算宇宙年龄；伟大如宇宙，终究需要智慧生命来见证自身的存在。当哈勃望远镜传回\"创生之柱\"的影像时，那既是星云孕育新恒星的摇篮，也是人类理性与浪漫交织的丰碑——在广漠时空中，我们既是朝生暮死的蜉蝣，也是宇宙认识自我的眼睛。\n',2,7,3),('19','3','37user','未知：暗物质的永恒谜题','2025-06-10 14:11:13','\n\n\n\n**星辰：宇宙历史的活化石**\n猎户座腰带三星的光芒，实则是八百年前发出的古老信使；仙女座星系模糊的光斑，展现的是二百五十万年前的宇宙图景。望远镜不仅是空间探测器，更是时间机器——我们观测得越深远，便越接近宇宙的童年。超新星遗迹中检测到的重元素，揭示了我们身体里的钙与铁皆源自垂死恒星的剧烈爆发。正如卡尔·萨根所言：\"我们是星尘的自觉体。\"\n\n**尺度：颠覆认知的维度课堂**\n地球在太阳系中尚不如沙滩上的一粒细沙，而太阳系本身在银河系旋臂中不过是一个暗淡的光点。可观测宇宙中约两万亿个星系，每个星系又包含千亿颗恒星，这种尺度足以让任何人类中心的幻想瓦解。当旅行者1号在64亿公里外回望地球，拍下那粒\"暗淡蓝点\"时，人类第一次直观认识到：所有文明史都发生在这个悬浮在太阳光束中的微尘之上。\n\n**未知：暗物质的永恒谜题**\n可观测物质仅占宇宙总质能的5%，剩余95%由暗物质与暗能量构成——这堪称现代科学最大的\"认知黑洞\"。引力透镜现象证明暗物质确实存在，但至今无法直接探测；暗能量更推动着宇宙加速膨胀，却连理论模型都充满矛盾。正如爱因斯坦感叹：\"宇宙最不可理解之处，在于它居然可以被理解。\"每解开一个天文谜题，往往暴露出更深层的未知领域。\n\n在智利阿塔卡马沙漠，ALMA射电望远镜阵列正接收130亿年前的第一代恒星信号；在贵州群山之中，\"中国天眼\"FAST监听可能的外星文明电波。这些现代巴别塔延续着祖先的仰望姿态，只是追问已从\"星星是什么\"升级为\"我们在多重宇宙中是否唯一\"。\n\n或许天文的终极价值，不在于找到所有答案，而在于它永恒提醒着人类的渺小与伟大。渺小如我们，竟能测量星系旋转曲线，推算宇宙年龄；伟大如宇宙，终究需要智慧生命来见证自身的存在。当哈勃望远镜传回\"创生之柱\"的影像时，那既是星云孕育新恒星的摇篮，也是人类理性与浪漫交织的丰碑——在广漠时空中，我们既是朝生暮死的蜉蝣，也是宇宙认识自我的眼睛。\n',2,1,1),('37','3','13user','明天放假','2025-07-16 11:31:47','\n 快速开始\n我们将通过一个简单的 Demo 来阐述 MyBatis-Plus 的强大功能，在此之前，我们假设您已经：\n\n拥有 Java 开发环境以及相应 IDE\n熟悉 Spring Boot\n熟悉 Maven 或 Gradle\n现有一张 User 表，其表结构如下：\n\nid	name	age	email\n1	Jone	18	test1@baomidou.com\n2	Jack	20	test2@baomidou.com\n3	Tom	28	test3@baomidou.com\n4	Sandy	21	test4@baomidou.com\n5	Billie	24	test5@baomidou.com\n其对应的数据库 Schema 脚本如下：\n\nschema-h2.sql\nDROP TABLE IF EXISTS `user`;\n\nCREATE TABLE `user`\n(\n    id BIGINT NOT NULL COMMENT \'主键ID\',\n    name VARCHAR(30) NULL DEFAULT NULL COMMENT \'姓名\',\n    age INT NULL DEFAULT NULL COMMENT \'年龄\',\n    email VARCHAR(50) NULL DEFAULT NULL COMMENT \'邮箱\',\n    PRIMARY KEY (id)\n);\n\n其对应的数据库 Data 脚本如下：\n\ndata-h2.sql\nDELETE FROM `user`;\n\nINSERT INTO `user` (id, name, age, email) VALUES\n(1, \'Jone\', 18, \'test1@baomidou.com\'),\n(2, \'Jack\', 20, \'test2@baomidou.com\'),\n(3, \'Tom\', 28, \'test3@baomidou.com\'),\n(4, \'Sandy\', 21, \'test4@baomidou.com\'),\n(5, \'Billie\', 24, \'test5@baomidou.com\');\n\n你知道吗？\n\n如果从零开始用 MyBatis-Plus 来实现该表的增删改查我们需要做什么呢？\n\n初始化工程\n创建一个空的 Spring Boot 工程，加入 H2 数据库进行集成测试。\n\n提示\n\n点此 Spring Initializer 可快速初始化一个 Spring Boot 工程\n',15,15,14),('463fa9a9954e4c9dae220fea4c23a8d9','4','3user','午夜电话亭：消失的第十三位来电者','2025-07-17 14:57:37','#### 第一章：雨夜的红色电话亭\n\n凌晨2点17分，伦敦东区，暴雨如注。\n\n巡警汤姆·霍华德在路过老贝克街时，发现那座本该被拆除的红色电话亭里亮着灯。透过模糊的玻璃，能看见一个穿黑色风衣的男人正握着听筒说话——这很诡异，因为市政记录显示，这条街的电话亭早在三年前就断开了线路。\n\n当汤姆敲开玻璃门的瞬间，听筒从男人手中滑落，悬挂的电线像蛇一样扭动。亭内空无一人，只有听筒里传来沙沙的杂音，以及一个机械重复的女声：\'请转告劳拉...第十三个...\'\n\n#### 第二章：泛黄的日记本\n\n三天后，古董店老板劳拉收到一个没有寄件人的包裹。里面是一本1947年的警用记事本，其中被撕掉的第13页边缘残留着暗褐色污渍。\n\n当她用紫外线灯照射时，空白处浮现出用隐形墨水写下的名单：\n1. 詹姆斯·威尔逊（1947.11.2）\n2. 艾琳·卡特（1947.11.9）\n...\n12. 托马斯·霍华德（2023.10.31）\n\n最后一行墨迹新鲜得像是刚刚写下：\n**13. 劳拉·格林**\n\n#### 关键线索（代码形式存储的谜题）\n```python\n# 电话亭键盘数字对应的字母\nclue = {\n    \'2\': [\'A\',\'B\',\'C\'],\n    \'5\': [\'J\',\'K\',\'L\'],\n    \'8\': [\'T\',\'U\',\'V\']\n}\n# 死者最后接到的电话记录\nlast_call = \'555-8282\'  # 拼写出来是 \'JKL-TUTU\'\n```\n\n#### 终章：闭环\n当劳拉在午夜踏入电话亭，听筒自动贴到她耳边。她终于听清了那个始终被雨声掩盖的单词——不是\'转告\'，而是\'成为\'。\n\n玻璃门外，汤姆警员惊恐地看着劳拉的身影在电流声中分解重组，变成穿黑风衣的男人。而市政档案显示，这座电话亭的编号，正是13。',1,1,0),('7','1','37user','休息：那并非空白，而是生命的留白','2025-06-12 14:11:13','\n世人常将休息误解为空白、懈怠，或奢侈的停顿——它被匆忙的日程挤压，被“永动”的幻觉所鄙夷。然而，真正的休息绝非生命的留白，而是生命必要的留白，一种深沉的智慧，一种被严重低估的再生力。\n\n身体：休耕方有沃土。 机器昼夜不息终将崩坏，身体亦复如是。规律睡眠远非奢侈，而是细胞修复、毒素清理的精密工程。当紧绷的肌肉在真正放松中卸下重担，当疲惫的神经在深沉睡眠里重获秩序，我们才为明日的行动蓄满能量。如同土地需要休耕，身体也需这看似“无为”的间歇，才能避免过早的枯竭，成为承载活力的沃土。\n\n心灵：沉静方得澄明。 真正高级的休息，在于让喧嚣的心沉静。当我们从信息的洪流、目标的追逐中短暂抽身，允许思绪如云般自然漂浮，而非被焦虑驱策，内在的秩序便开始悄然恢复。此时，压力如潮水退去，创造力的微光得以在宁静的水面浮现。爱因斯坦那些划时代的洞见，往往诞生于拉小提琴的悠然时刻，而非伏案疾书的紧绷之中。这片刻的“放空”，恰是心灵澄明、孕育灵光的深潭。\n\n创造：孕育需待静默。 休息更是创造力的隐秘温床。达芬奇深知此道，他会在紧张绘制《蒙娜丽莎》的间隙，放下画笔，长久凝视作品，甚至转身离去。这些有意识的“暂停”，绝非放弃，而是让潜意识在后台无声工作，最终让那抹神秘的微笑跃然画布。灵感从不诞生于持续的挤压，它需要呼吸的空间，在看似无所事事的静默中，悄然酝酿成形。\n\n因此，请郑重地将休息纳入生命的节律吧。这不是对责任的逃避，而是对生命深沉的负责。允许自己暂时脱离永不停转的轨道，去体味一段无目的的散步、一次纯粹的凝视、一刻与自我独处的安宁。学会在纷繁中按下暂停键，如同大地在季节轮替中休养生息，方能积蓄生生不息的力量。\n\n休息并非时间的浪费，而是对时间的另一种投资，一种更深的抵达。 它是对疲惫的温柔抵抗，是对创造力的无声召唤，更是对生命本身的庄严敬意——在看似停顿的空白里，蕴藏着重新书写活力的深邃留白。\n',12,11,9),('791b6bb9f37c4906bc8fe65c370cdf2f','0','3user','AI辅助话语','2025-07-17 15:51:20','我现在完善个人博客系统登录页面，我的要求为功能为可以根据选择来选择是用户登录或者管理员登录。登录成功后，后端会返回数据，需要将该数据存储到本地仓库中（localStorage），并命名为login代码如（localStorage.setItem(\'login\', JSON.stringify(response.data))）\n相关属性（\nconst loginForm = ref({\n    id: \'\',\n    password: \'\',\n    captchaId: \'\',\n    captchaCode: \'\'\n    \n})\n）\n用户登录成功后，跳转到\'/users\'\n管理员登录成功后，跳转到\'/admins\'\n相关功能的相关请求被封装在了\'@/api/login\',被封装的请求有(\n\n请根据要求完善该代码，对了验证码要有刷新功能，进入该页面会自动生成验证码\n采用现代简约设计风格，整体布局清晰美观，使用Element Plus的默认主题色彩方案，请根据要求基于vue3和ElementPlus来设计页面。\n',4,12,2),('792a02bb4fc94c2db2a3df4a075c68de','0','3user','Spring Boot 入门指南：从零开始构建RESTful API','2025-07-17 14:51:18','Spring Boot 是一个基于 Spring 框架的快速开发工具，它简化了 Spring 应用的初始搭建和开发过程。本文将带你从零开始构建一个完整的 RESTful API，涵盖以下内容：\n\n1. **环境准备**\n   - JDK 17+ 安装\n   - Maven 或 Gradle 构建工具\n   - IntelliJ IDEA 或 VS Code 开发环境\n\n2. **创建 Spring Boot 项目**\n   - 使用 Spring Initializr (https://start.spring.io/) 生成项目模板\n   - 选择依赖：Spring Web、Lombok、Spring Data JPA\n\n3. **编写第一个 Controller**\n   ```java\n   @RestController\n   @RequestMapping(\"/api/articles\")\n   public class ArticleController {\n       @GetMapping\n       public String hello() {\n           return \"Hello, Spring Boot!\";\n       }\n   }\n   ```\n\n4. **数据库集成（MySQL + JPA）**\n   - 配置 `application.properties`\n   - 创建 `Article` 实体类\n   - 使用 `JpaRepository` 进行 CRUD 操作\n\n5. **测试 API**\n   - 使用 Postman 或 curl 发送请求\n   - 示例：`GET http://localhost:8080/api/articles`\n\n6. **部署到生产环境**\n   - 打包成 JAR 文件：`mvn package`\n   - 使用 Docker 容器化部署\n\n希望这篇教程能帮助你快速上手 Spring Boot 开发！',2,3,0),('8','1','3user','仰望：人类永恒的星空之问','2025-06-11 14:11:13','\n\n**尺度：颠覆认知的维度课堂**\n地球在太阳系中尚不如沙滩上的一粒细沙，而太阳系本身在银河系旋臂中不过是一个暗淡的光点。可观测宇宙中约两万亿个星系，每个星系又包含千亿颗恒星，这种尺度足以让任何人类中心的幻想瓦解。当旅行者1号在64亿公里外回望地球，拍下那粒\"暗淡蓝点\"时，人类第一次直观认识到：所有文明史都发生在这个悬浮在太阳光束中的微尘之上。\n\n**未知：暗物质的永恒谜题**\n可观测物质仅占宇宙总质能的5%，剩余95%由暗物质与暗能量构成——这堪称现代科学最大的\"认知黑洞\"。引力透镜现象证明暗物质确实存在，但至今无法直接探测；暗能量更推动着宇宙加速膨胀，却连理论模型都充满矛盾。正如爱因斯坦感叹：\"宇宙最不可理解之处，在于它居然可以被理解。\"每解开一个天文谜题，往往暴露出更深层的未知领域。\n\n在智利阿塔卡马沙漠，ALMA射电望远镜阵列正接收130亿年前的第一代恒星信号；在贵州群山之中，\"中国天眼\"FAST监听可能的外星文明电波。这些现代巴别塔延续着祖先的仰望姿态，只是追问已从\"星星是什么\"升级为\"我们在多重宇宙中是否唯一\"。\n\n或许天文的终极价值，不在于找到所有答案，而在于它永恒提醒着人类的渺小与伟大。渺小如我们，竟能测量星系旋转曲线，推算宇宙年龄；伟大如宇宙，终究需要智慧生命来见证自身的存在。当哈勃望远镜传回\"创生之柱\"的影像时，那既是星云孕育新恒星的摇篮，也是人类理性与浪漫交织的丰碑——在广漠时空中，我们既是朝生暮死的蜉蝣，也是宇宙认识自我的眼睛。\n',3,10,3),('95427c4b627c42099d113af68f3ee480','5','3user','【Neo-Tokyo 2089】我在黑市装了非法神经插件后的72小时','2025-07-17 15:46:50','从那个叫「赛博药师」的义体医生那里拿到了用比特币换来的「灵蝶3.0」神经插件。安装时闻到自己脑浆被激光灼烧的焦糊味——这该死的盗版插件连麻醉功能都没装。  \n\n\n插件开始生效后，整个世界变成了由发光数据流构成的网格。更可怕的是我能看到人们头顶漂浮着他们的：  \n- 信用评分（那个流浪汉居然是AAA级）  \n- 犯罪概率（便利店店员显示87%）  \n- 最深的秘密（...我宁愿没看到女友的数据）  \n\n\n三菱重工的安全部队正在扫描每个路人的脑波频率。我把神经插件的散热片拆下来当刀片用，在涩谷全息广告牌的掩护下，往那个永远在施工的量子隧道跑去...',1,1,1),('db5fd154090941e2b99253d65b94fe6b','1','3user','iPhone 16 Pro 首次采用钛合金曲面边框','2025-07-17 15:41:16','#### 一、外观设计\\n\\niPhone 16 Pro 首次采用钛合金曲面边框，重量减轻15%的同时抗跌落性能提升20%。我们拿到的「星空黑」配色在光线折射下会浮现出类似银河的细微光点——这可能是苹果史上最复杂的阳极氧化工艺。\\n\\n#### 二、颠覆性交互\\n\\n1. **全息投影键盘**（需搭配AirVision Pro使用）\\n   实测输入速度比触屏快40%，但学习曲线陡峭\\n   \\n2. **脑电波辅助操作**\\n   通过新增的神经传感器，可以用皱眉动作挂断电话（灵敏度需调校）\\n\\n#### 三、性能怪兽\\n```python\n# A18 Pro芯片跑分对比\ngeekbench_score = {\n    \"iPhone 15 Pro\": 2850,\n    \"iPhone 16 Pro\": 4213,  # 单核性能提升48%\n    \"Galaxy S24 Ultra\": 3890\n}\n```\\n\\n#### 四、争议点\\n- **摄像头凸起更夸张**：三摄模组厚度达4.2mm，平放桌面会跷跷板\\n- **欧盟特供版**：因法规要求，侧边新增物理静音滑块（其他地区仍为触控）\\n\\n#### 五、购买建议\\n适合人群：\\n✅ 追求极致性能的科技极客\\n✅ 苹果生态重度用户\\n🚫 预算有限的消费者（起售价涨至$1199）',2,7,0);
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_tag`
--

DROP TABLE IF EXISTS `article_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_tag` (
  `article_tag_id` varchar(50) NOT NULL COMMENT '文章标签id',
  `article_tag_name` varchar(35) DEFAULT NULL COMMENT '标签名称',
  `article_tag_add_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`article_tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_tag`
--

LOCK TABLES `article_tag` WRITE;
/*!40000 ALTER TABLE `article_tag` DISABLE KEYS */;
INSERT INTO `article_tag` VALUES ('0','AI','2025-06-10 14:11:13'),('1','深度学习','2025-06-09 14:11:13'),('11','Spring Boot','2025-05-30 14:11:13'),('12','后端开发','2025-05-29 14:11:13'),('13','Restful API','2025-05-28 14:11:13'),('14','Java','2025-05-27 14:11:13'),('15','心理悬疑','2025-05-26 14:11:13'),('16','时间循环','2025-05-25 14:11:13'),('17','都市传说','2025-05-24 14:11:13'),('18','解谜元素','2025-05-23 14:11:13'),('19','文章标签19','2025-05-22 14:11:13'),('2','文章标签2','2025-06-08 14:11:13'),('20','文章标签20','2025-05-21 14:11:13'),('21','文章标签21','2025-05-20 14:11:13'),('22','文章标签22','2025-05-19 14:11:13'),('23','文章标签23','2025-05-18 14:11:13'),('24','文章标签24','2025-05-17 14:11:13'),('25','文章标签25','2025-05-16 14:11:13'),('26','文章标签26','2025-05-15 14:11:13'),('27','文章标签27','2025-05-14 14:11:13'),('28','文章标签28','2025-05-13 14:11:13'),('29','文章标签29','2025-05-12 14:11:13'),('4','文章标签4','2025-06-06 14:11:13'),('5','文章标签5','2025-06-05 14:11:13'),('6','文章标签6','2025-06-04 14:11:13'),('6838333617324c5f8ba928e2fe81fb99','HTML','2025-06-18 22:44:55'),('7','文章标签7','2025-06-03 14:11:13'),('8','文章标签8','2025-06-02 14:11:13'),('8ef5bd5fee0d4ccda7c9dd298f9c1c7c','JS','2025-06-18 22:44:50'),('9','文章标签9','2025-06-01 14:11:13'),('ab043d9b182c4bb3890648f0b6b7e8e1','王者','2025-06-18 22:47:13');
/*!40000 ALTER TABLE `article_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_tag_list`
--

DROP TABLE IF EXISTS `article_tag_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_tag_list` (
  `article_tag_list_id` varchar(50) NOT NULL COMMENT '文章对应标签id',
  `article_id` varchar(50) DEFAULT NULL COMMENT '文章id',
  `article_tag_id` varchar(50) DEFAULT NULL COMMENT '文章标签id',
  PRIMARY KEY (`article_tag_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_tag_list`
--

LOCK TABLES `article_tag_list` WRITE;
/*!40000 ALTER TABLE `article_tag_list` DISABLE KEYS */;
INSERT INTO `article_tag_list` VALUES ('0c6734f8948f4966b19df51068e2ea55','95427c4b627c42099d113af68f3ee480','17'),('1','1','27'),('10','10','25'),('102fe7d108514f0fb9d4673a583a3578','463fa9a9954e4c9dae220fea4c23a8d9','17'),('11','11','11'),('12','12','4'),('13','13','14'),('14','14','15'),('15','15','15'),('16','16','24'),('17','17','1'),('18','18','0'),('19','19','27'),('1ae7a3928055417a97d2d26285d6bc04','463fa9a9954e4c9dae220fea4c23a8d9','15'),('2','2','4'),('20','20','13'),('21','21','6'),('22','22','2'),('23','23','29'),('24','24','10'),('25','1','2'),('3','3','28'),('4','4','4'),('5','5','29'),('598f497700a24dfa9c9ace4f84627a72','463fa9a9954e4c9dae220fea4c23a8d9','16'),('5ec2dc8297744cf58cede1b413a025e5','95427c4b627c42099d113af68f3ee480','16'),('6','6','22'),('629e36a56efa45ae912ded470df3fee4','792a02bb4fc94c2db2a3df4a075c68de','11'),('62d3057d43674040939a33aea22d04f0','463fa9a9954e4c9dae220fea4c23a8d9','18'),('7','7','13'),('8','8','28'),('8d22f1ea96bb47fc8b8deff33e5ad4fe','791b6bb9f37c4906bc8fe65c370cdf2f','0'),('8f6bf0bbae8340fe98e8dfeff06f240d','792a02bb4fc94c2db2a3df4a075c68de','12'),('9','9','20'),('be5f4a315879489a9265ecc3b0412c99','792a02bb4fc94c2db2a3df4a075c68de','14'),('c2780858bcdd478faf8adc50254379dc','db5fd154090941e2b99253d65b94fe6b','0');
/*!40000 ALTER TABLE `article_tag_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_type`
--

DROP TABLE IF EXISTS `article_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_type` (
  `article_type_id` varchar(50) NOT NULL COMMENT '文章类型id',
  `article_type_name` varchar(50) DEFAULT NULL COMMENT '文章类型名',
  `article_type_sort` int DEFAULT NULL COMMENT '排序（序号越小越靠前）',
  `article_type_add_time` datetime DEFAULT NULL COMMENT '文章类型添加时间',
  PRIMARY KEY (`article_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_type`
--

LOCK TABLES `article_type` WRITE;
/*!40000 ALTER TABLE `article_type` DISABLE KEYS */;
INSERT INTO `article_type` VALUES ('0','技术前沿',1,'2025-07-18 09:55:48'),('1','悬疑故事',2,'2025-07-18 09:55:48'),('2','数码评测',3,'2025-07-18 09:55:48'),('3','美食探秘',4,'2025-07-18 09:55:48'),('4','社会观察',5,'2025-07-18 09:55:48'),('5','赛博幻想',6,'2025-07-18 09:55:48'),('6','生活指南',7,'2025-07-18 09:55:48'),('7','深度报道',8,'2025-07-18 09:55:48');
/*!40000 ALTER TABLE `article_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_id` varchar(50) NOT NULL COMMENT '评论id',
  `article_id` varchar(50) DEFAULT NULL COMMENT '文章id',
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户id(评论人)',
  `comment_content` text COMMENT '评论内容',
  `comment_time` datetime DEFAULT NULL COMMENT '评论时间',
  `comment_good_number` int DEFAULT NULL COMMENT '点赞量',
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES ('138be92486a04fbdbf9c233a0db74e39','13','13user','或许我快完成了','2025-07-18 22:37:06',1),('84e0e98a6e1e498bb786b0a4a2f3d22f','791b6bb9f37c4906bc8fe65c370cdf2f','37user','这篇文章写的很不错','2025-07-19 19:14:40',1),('c1','13','123123','黑洞的事件视界真的是单向膜吗？','2023-01-01 10:00:00',56),('c1dd37fb1fbb42889742daa7e06adb71','792a02bb4fc94c2db2a3df4a075c68de','37user','Test','2025-07-19 20:34:53',1),('c4','13','21user','行星形成的吸积盘理论需要更多观测数据支持。','2023-01-01 16:45:00',23),('c49','13','3user','天体文明还是太超前来','2025-07-18 19:52:33',11),('c5','13','37user','引力波探测技术未来会有更大突破。','2023-01-01 18:20:00',46),('c50','13','3user','SETI计划真的能发现外星文明吗？','2023-02-28 15:30:00',13),('c6','13','14user','外星文明会不会将国内占领','2025-07-18 19:54:53',37),('cf76e1024bad49ed9e2ab3b74d0a4a70',NULL,'37user','我收回刚才说的话','2025-07-19 19:14:51',0),('e96d0172d72f4dea87cc2cec4fbadaf1',NULL,'13user','你觉的呢？','2025-07-18 22:55:15',0),('fe20145ee3de490cb742f5e0208b8031',NULL,'13user','我很同意你的观点','2025-07-18 22:51:48',1);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_reply`
--

DROP TABLE IF EXISTS `comment_reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_reply` (
  `comment_reply_id` varchar(50) NOT NULL COMMENT '评论回复id',
  `first_comment_id` varchar(50) DEFAULT NULL COMMENT '一级评论',
  `second_comment_id` varchar(50) DEFAULT NULL COMMENT '二级评论',
  `reply_user_id` varchar(50) DEFAULT NULL COMMENT '回复评论人的id',
  PRIMARY KEY (`comment_reply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_reply`
--

LOCK TABLES `comment_reply` WRITE;
/*!40000 ALTER TABLE `comment_reply` DISABLE KEYS */;
INSERT INTO `comment_reply` VALUES ('1f53aafc921441478a7cf4e02a7c34c0','c1','fe20145ee3de490cb742f5e0208b8031','37user'),('2eccae73eeb843e396c3351fab501ef7','84e0e98a6e1e498bb786b0a4a2f3d22f','cf76e1024bad49ed9e2ab3b74d0a4a70','37user'),('9059a4a84d284eeb8417f47988136f59','c50','e96d0172d72f4dea87cc2cec4fbadaf1','3user'),('r100','c1','c4','3user'),('r2','c1','c5','123123'),('r3','c1','c6','21user');
/*!40000 ALTER TABLE `comment_reply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link`
--

DROP TABLE IF EXISTS `link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `link` (
  `link_id` varchar(50) NOT NULL COMMENT '友情链接id',
  `link_title` varchar(150) DEFAULT NULL COMMENT '友情链接标题',
  `link_url` varchar(500) DEFAULT NULL COMMENT '友情链接地址',
  `link_logo_url` varchar(500) DEFAULT NULL COMMENT '友情链接logo',
  `link_sort` int DEFAULT '7' COMMENT '排序（序号越小越靠前）',
  `link_add_time` datetime DEFAULT NULL COMMENT '添加友情链接的时间',
  PRIMARY KEY (`link_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link`
--

LOCK TABLES `link` WRITE;
/*!40000 ALTER TABLE `link` DISABLE KEYS */;
INSERT INTO `link` VALUES ('1581b72b013f43739ae7a4cad89d23d6','deepseek','https://www.deepseek.com','https://cdn.deepseek.com/logo.png?x-image-process=image%2Fresize%2Cw_1920',3,'2025-06-21 01:10:26'),('37bc25adcbf8436ca37736d437e4b943','NodeFree','https://nodefree.net/f/freenode','https://nodefree.net/wp-content/uploads/2022/04/icon.png',0,'2025-06-21 01:18:23'),('7a8172cd9d69452ba18591a321f8d830','百度','https://baidu.com','https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png',1,'2025-06-20 17:45:03'),('9d232896e6b340be8b0eae7f466cfd21','豆包','https://www.doubao.com/','https://lf-flow-web-cdn.doubao.com/obj/flow-doubao/samantha/logo-icon-white-bg.png',2,'2025-06-21 01:05:52'),('cf10b857a78f44369452018efd13da2c','ElementPlus','https://element-plus.org/zh-CN/','https://element-plus.org/images/element-plus-logo.svg',1,'2025-06-21 01:17:27');
/*!40000 ALTER TABLE `link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` varchar(50) NOT NULL COMMENT '用户id',
  `user_avatar` varchar(500) DEFAULT NULL COMMENT '用户头像url',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `user_password` varchar(50) DEFAULT NULL COMMENT '用户密码',
  `user_frozen` int DEFAULT '0' COMMENT '冻结（0：正常，1：冻结）',
  `user_register_time` datetime DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('123123','https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/users/626f03e3a58147d285a6520b7e76b644.jpeg','Lim','123456',0,'2025-06-09 23:29:02'),('12user',NULL,'12UName','123456',1,'2024-06-08 13:53:40'),('13user','https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/users/71baa647423e4e2d8d99ae7e65866720.png','y-37','123456',0,'2024-05-08 13:53:40'),('14user','https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/users/b372f577545940768b30dcdb1a5134ca.jpeg','Nicy','123456',0,'2024-04-08 13:53:40'),('15user',NULL,'15xiaodai','123456',1,'2024-03-08 13:53:40'),('16user',NULL,'16UName','123456',0,'2024-02-08 13:53:40'),('17user',NULL,'17UName','123456',0,'2024-01-08 13:53:40'),('18user',NULL,'18UName','123456',0,'2023-12-08 13:53:40'),('19user',NULL,'19UName','123456',0,'2023-11-08 13:53:40'),('1user',NULL,'1UName','123456',0,'2025-05-08 13:53:40'),('20user',NULL,'20UName','123456',0,'2023-10-08 13:53:40'),('21user','https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/users/28e3e332803549fa84a5a5e7f04d35f5.jpeg','Flower','123456',0,'2023-09-08 13:53:40'),('22user',NULL,'22UName','123456',0,'2023-08-08 13:53:40'),('23user',NULL,'23UName','123456',0,'2023-07-08 13:53:40'),('24user',NULL,'24UName','123456',0,'2023-06-08 13:53:40'),('26user',NULL,'26UName','123456',0,'2023-04-08 13:53:40'),('27user',NULL,'27UName','123456',0,'2023-03-08 13:53:40'),('28user',NULL,'28UName','123456',0,'2023-02-08 13:53:40'),('29user',NULL,'29UName','123456',0,'2023-01-08 13:53:40'),('2user',NULL,'2UName','123456',0,'2025-04-08 13:53:40'),('30user',NULL,'30UName','123456',0,'2022-12-08 13:53:40'),('31user',NULL,'31UName','123456',0,'2022-11-08 13:53:40'),('32user',NULL,'32UName','123456',0,'2022-10-08 13:53:40'),('33user',NULL,'33UName','123456',0,'2022-09-08 13:53:40'),('34user',NULL,'34UName','123456',0,'2022-08-08 13:53:40'),('35user',NULL,'35UName','123456',0,'2022-07-08 13:53:40'),('36user',NULL,'36UName','123456',0,'2022-06-08 13:53:40'),('37user','https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/users/7de2f53e8d8c48a28f5832099e2479de.jpeg','Chrim','123456',0,'2022-05-08 13:53:40'),('38user',NULL,'38UName','123456',0,'2022-04-08 13:53:40'),('39user',NULL,'39UName','123456',0,'2022-03-08 13:53:40'),('3user','https://y-37.oss-cn-beijing.aliyuncs.com/blog-system/users/4f6446b1a354447992ca4e8c138ef3f6.jpeg','Limo','123456',0,'2025-03-08 13:53:40'),('40user',NULL,'40UName','123456',0,'2022-02-08 13:53:40'),('41user',NULL,'41UName','123456',0,'2022-01-08 13:53:40'),('42user',NULL,'42UName','123456',0,'2021-12-08 13:53:40'),('43user',NULL,'43UName','123456',0,'2021-11-08 13:53:40'),('44user',NULL,'44UName','123456',0,'2021-10-08 13:53:40'),('45user',NULL,'45UName','123456',0,'2021-09-08 13:53:40'),('46user',NULL,'46UName','123456',0,'2021-08-08 13:53:40'),('47user',NULL,'47UName','123456',0,'2021-07-08 13:53:40'),('48user',NULL,'48UName','123456',0,'2021-06-08 13:53:40'),('49user',NULL,'49UName','123456',0,'2021-05-08 13:53:40'),('4user',NULL,'4UName','123456',0,'2025-02-08 13:53:40'),('50user',NULL,'50UName','123456',0,'2021-04-08 13:53:40'),('51user',NULL,'51UName','123456',0,'2021-03-08 13:53:40'),('52user',NULL,'52UName','123456',0,'2021-02-08 13:53:40'),('53user',NULL,'53UName','123456',0,'2021-01-08 13:53:40'),('54user',NULL,'54UName','123456',0,'2020-12-08 13:53:40'),('55user',NULL,'55UName','123456',0,'2020-11-08 13:53:40'),('56user',NULL,'56UName','123456',0,'2020-10-08 13:53:40'),('57user',NULL,'57UName','123456',0,'2020-09-08 13:53:40'),('58user',NULL,'58UName','123456',0,'2020-08-08 13:53:40'),('59user',NULL,'59UName','123456',0,'2020-07-08 13:53:40'),('5user',NULL,'5UName','123456',0,'2025-01-08 13:53:40'),('60user',NULL,'60UName','123456',0,'2020-06-08 13:53:40'),('61user',NULL,'61UName','123456',0,'2020-05-08 13:53:40'),('62user',NULL,'62UName','123456',0,'2020-04-08 13:53:40'),('63user',NULL,'63UName','123456',0,'2020-03-08 13:53:40'),('64user',NULL,'64UName','123456',0,'2020-02-08 13:53:40'),('65user',NULL,'65UName','123456',0,'2020-01-08 13:53:40'),('66user',NULL,'66UName','123456',0,'2019-12-08 13:53:40'),('67user',NULL,'67UName','123456',0,'2019-11-08 13:53:40'),('68user',NULL,'68UName','123456',0,'2019-10-08 13:53:40'),('69user',NULL,'69UName','123456',0,'2019-09-08 13:53:40'),('6user',NULL,'6UName','123456',0,'2024-12-08 13:53:40'),('70user',NULL,'70UName','123456',0,'2019-08-08 13:53:40'),('71user',NULL,'71UName','123456',0,'2019-07-08 13:53:40'),('72user',NULL,'72UName','123456',0,'2019-06-08 13:53:40'),('73user',NULL,'73UName','123456',0,'2019-05-08 13:53:40'),('74user',NULL,'74UName','123456',0,'2019-04-08 13:53:40'),('75user',NULL,'75UName','123456',0,'2019-03-08 13:53:40'),('76user',NULL,'76UName','123456',0,'2019-02-08 13:53:40'),('77user',NULL,'77UName','123456',0,'2019-01-08 13:53:40'),('78user',NULL,'78UName','123456',0,'2018-12-08 13:53:40'),('79user',NULL,'79UName','123456',0,'2018-11-08 13:53:40'),('7user',NULL,'7UName','123456',0,'2024-11-08 13:53:40'),('80user',NULL,'80UName','123456',0,'2018-10-08 13:53:40'),('81user',NULL,'81UName','123456',0,'2018-09-08 13:53:40'),('82user',NULL,'82UName','123456',0,'2018-08-08 13:53:40'),('83user',NULL,'83UName','123456',0,'2018-07-08 13:53:40'),('84user',NULL,'84UName','123456',0,'2018-06-08 13:53:40'),('85user',NULL,'85UName','123456',0,'2018-05-08 13:53:40'),('86user',NULL,'86UName','123456',0,'2018-04-08 13:53:40'),('87user',NULL,'87UName','123456',0,'2018-03-08 13:53:40'),('88user',NULL,'88UName','123456',0,'2018-02-08 13:53:40'),('89user',NULL,'89UName','123456',0,'2018-01-08 13:53:40'),('8user',NULL,'8UName','123456',0,'2024-10-08 13:53:40'),('90user',NULL,'90UName','123456',0,'2017-12-08 13:53:40'),('91user',NULL,'91UName','123456',0,'2017-11-08 13:53:40'),('92user',NULL,'92UName','123456',0,'2017-10-08 13:53:40'),('93user',NULL,'93UName','123456',0,'2017-09-08 13:53:40'),('94user',NULL,'94UName','123456',0,'2017-08-08 13:53:40'),('95user',NULL,'95UName','123456',0,'2017-07-08 13:53:40'),('96user',NULL,'96UName','123456',0,'2017-06-08 13:53:40'),('97user',NULL,'97UName','123456',0,'2017-05-08 13:53:40'),('98user',NULL,'98UName','123456',0,'2017-04-08 13:53:40'),('99user',NULL,'99UName','123456',0,'2017-03-08 13:53:40'),('9user',NULL,'9UName','123456',0,'2024-09-08 13:53:40');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_fan`
--

DROP TABLE IF EXISTS `user_fan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_fan` (
  `user_fan_id` varchar(50) NOT NULL COMMENT '主键id',
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户id',
  `fan_id` varchar(50) DEFAULT NULL COMMENT '粉丝id',
  `create_time` datetime DEFAULT NULL COMMENT '关注时间',
  PRIMARY KEY (`user_fan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户粉丝表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_fan`
--

LOCK TABLES `user_fan` WRITE;
/*!40000 ALTER TABLE `user_fan` DISABLE KEYS */;
INSERT INTO `user_fan` VALUES ('146538e62fbc49a1920bc33f3c59e81f','3user','21user','2025-07-18 09:09:25'),('23db606b49ba489b921e633804bd7a42','37user','13user','2025-07-17 16:00:48'),('52206fb01ea848db9cbd216344c02206','3user','13user','2025-07-17 16:00:45'),('721b9f81d9c14c859a5a6143a7b4be01','13user','21user','2025-07-18 09:09:41'),('7593871fd012404abb223cc6657addcc','37user','3user','2025-07-17 15:53:48'),('8d3e1b890dfe4e0da75ad65c8cf31f81','37user','21user','2025-07-18 09:09:30'),('adca75a3c14144ccb84e2f48f697361c','13user','3user','2025-07-17 20:21:12'),('b5a14047f1e64b47a7a0646be1a3c002','14user','3user','2025-07-17 15:53:41'),('UFI001','14user','13user','2025-07-17 08:21:35'),('UFI002','14user','15user','2025-07-17 08:21:56'),('UFI003','13user','14user','2025-07-17 08:22:15');
/*!40000 ALTER TABLE `user_fan` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-19 20:54:02
