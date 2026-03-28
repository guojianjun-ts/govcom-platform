-- 使用数据库
USE `pu_yu`;

-- =====================================================
-- 1. 资讯分类表 (info_category)
-- =====================================================
DROP TABLE IF EXISTS `info_category`;
CREATE TABLE `info_category` (
                                 `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                 `categoryName` VARCHAR(50) NOT NULL COMMENT '分类名称',
                                 `categoryCode` VARCHAR(50) COMMENT '分类编码',
                                 `scope` TINYINT DEFAULT 1 COMMENT '适用范围：1-政务，2-社区，3-通用',
                                 `sortOrder` INT DEFAULT 0 COMMENT '排序',
                                 `icon` VARCHAR(50) COMMENT '图标',
                                 `status` TINYINT DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
                                 `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资讯分类表';

-- 插入分类数据
INSERT INTO `info_category` (`categoryName`, `categoryCode`, `scope`, `sortOrder`, `icon`) VALUES
                                                                                               ('要闻动态', 'news', 1, 1, 'fa-newspaper'),
                                                                                               ('政策解读', 'policy', 1, 2, 'fa-file-signature'),
                                                                                               ('政务通知', 'gov-notice', 1, 3, 'fa-bullhorn'),
                                                                                               ('社区通知', 'community-notice', 2, 4, 'fa-bell'),
                                                                                               ('活动公告', 'activity', 2, 5, 'fa-calendar-alt'),
                                                                                               ('社区风采', 'community-style', 2, 6, 'fa-images');

-- =====================================================
-- 2. 资讯内容表 (info_article)
-- =====================================================
DROP TABLE IF EXISTS `info_article`;
CREATE TABLE `info_article` (
                                `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                `categoryId` INT NOT NULL COMMENT '分类ID',
                                `title` VARCHAR(200) NOT NULL COMMENT '标题',
                                `summary` VARCHAR(500) COMMENT '摘要',
                                `content` TEXT COMMENT '内容（支持HTML）',
                                `cover` VARCHAR(500) COMMENT '封面图',
                                `author` VARCHAR(50) COMMENT '作者',
                                `source` VARCHAR(100) COMMENT '来源',
                                `scope` TINYINT DEFAULT 1 COMMENT '发布范围：1-全市，2-指定社区',
                                `communityId` INT COMMENT '指定社区ID（当scope=2时）',
                                `tag` VARCHAR(50) COMMENT '标签（多个用逗号分隔）',
                                `viewCount` INT DEFAULT 0 COMMENT '阅读次数',
                                `likeCount` INT DEFAULT 0 COMMENT '点赞次数',
                                `publishTime` DATETIME COMMENT '发布时间',
                                `isTop` TINYINT DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
                                `status` TINYINT DEFAULT 1 COMMENT '状态：0-草稿，1-已发布，2-下架',
                                `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                INDEX `idx_category` (`categoryId`),
                                INDEX `idx_scope_community` (`scope`, `communityId`),
                                INDEX `idx_publish` (`publishTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资讯内容表';

-- =====================================================
-- 3. 插入示例政务资讯数据
-- =====================================================
INSERT INTO `info_article` (
    `categoryId`, `title`, `summary`, `content`, `cover`,
    `author`, `source`, `scope`, `tag`, `viewCount`,
    `likeCount`, `publishTime`, `isTop`
) VALUES
      (
          1,
          '重庆市发布2026年智慧城市建设新规划',
          '重点聚焦民生服务领域，打造15分钟生活圈，实现政务服务“一网通办”全覆盖。',
          '<h3>智慧城市建设新规划</h3><p>近日，重庆市人民政府发布了《2026年智慧城市建设新规划》，提出到2026年底，基本建成“智慧渝生”服务体系。</p><p>规划重点包括：1. 政务服务“一网通办”全覆盖；2. 社区服务智能化升级；3. 民生数据共享互通。</p>',
          'https://picsum.photos/800/400?random=101',
          '重庆日报',
          '重庆日报',
          1,
          '重要,热点',
          1250,
          45,
          '2026-03-14 09:00:00',
          1
      ),
      (
          2,
          '医保新政解读：门诊报销比例提高至70%',
          '3月1日起实施，惠及全市参保人员，慢性病患者纳入保障范围。',
          '<h3>医保新政解读</h3><p>市医保局发布通知，从3月1日起，全市职工医保门诊报销比例提高至70%，年度最高支付限额提高至5000元。</p><p>此次调整将高血压、糖尿病等慢性病门诊用药纳入保障范围，预计惠及全市300万参保人员。</p>',
          'https://picsum.photos/800/400?random=102',
          '市医保局',
          '重庆医保',
          1,
          '政策',
          890,
          32,
          '2026-03-13 14:30:00',
          0
      ),
      (
          3,
          '关于优化政务服务流程的通知',
          '进一步压缩办理时限，推行“一网通办”，实现更多事项“一次不用跑”。',
          '<h3>政务服务优化通知</h3><p>市政务办发布通知，要求各区县进一步优化政务服务流程，压缩办理时限，推行“一网通办”。</p><p>通知明确，到2026年6月底前，90%的高频政务服务事项实现“一次不用跑”。</p>',
          'https://picsum.photos/800/400?random=103',
          '市政务办',
          '重庆政务',
          1,
          '通知',
          560,
          18,
          '2026-03-12 10:15:00',
          0
      );

-- =====================================================
-- 4. 插入示例社区资讯数据（大溪沟社区）
-- =====================================================
INSERT INTO `info_article` (
    `categoryId`, `title`, `summary`, `content`, `cover`,
    `author`, `source`, `scope`, `communityId`, `tag`,
    `viewCount`, `likeCount`, `publishTime`, `isTop`
) VALUES
      (
          4,
          '大溪沟社区停水通知',
          '因管道维修，3月15日9:00-17:00停水，请居民提前做好准备。',
          '<h3>停水通知</h3><p>接水务集团通知，因供水管道检修，大溪沟社区将于3月15日9:00-17:00停水。</p><p>影响范围：大溪沟社区全境。请居民提前储水，给您带来的不便敬请谅解。</p>',
          'https://picsum.photos/800/400?random=104',
          '物业中心',
          '大溪沟物业',
          2,
          101,
          '紧急',
          320,
          5,
          '2026-03-14 08:00:00',
          1
      ),
      (
          5,
          '社区亲子运动会报名开启',
          '3月20日周六上午9点，小区广场，欢迎居民踊跃报名参加。',
          '<h3>亲子运动会</h3><p>为了增进邻里感情，大溪沟社区将于3月20日（周六）上午9点在小区广场举办亲子运动会。</p><p>活动项目：三人四足、袋鼠跳、拔河比赛等。报名方式：联系物业中心或扫码报名。</p>',
          'https://picsum.photos/800/400?random=105',
          '居委会',
          '大溪沟居委会',
          2,
          101,
          '活动',
          210,
          12,
          '2026-03-13 16:20:00',
          0
      ),
      (
          6,
          '大溪沟社区最美庭院评选',
          '欢迎居民踊跃报名参加，展示您的庭院风采，赢取精美礼品。',
          '<h3>最美庭院评选</h3><p>大溪沟社区首届“最美庭院”评选活动开始啦！欢迎居民踊跃报名参加。</p><p>报名时间：3月15日-3月25日。评选方式：居民投票+评委打分。奖品设置：一等奖1名（智能扫地机器人），二等奖2名（空气炸锅），三等奖3名（电热水壶）。</p>',
          'https://picsum.photos/800/400?random=106',
          '社区工作组',
          '大溪沟社区',
          2,
          101,
          '评选',
          180,
          8,
          '2026-03-12 11:00:00',
          0
      );

-- =====================================================
-- 5. 插入示例社区资讯数据（海棠溪社区）
-- =====================================================
INSERT INTO `info_article` (
    `categoryId`, `title`, `summary`, `content`, `cover`,
    `author`, `source`, `scope`, `communityId`, `tag`,
    `viewCount`, `likeCount`, `publishTime`, `isTop`
) VALUES
      (
          4,
          '海棠溪社区垃圾分类宣传周',
          '3月16日-22日开展垃圾分类宣传活动，现场有礼品赠送。',
          '<h3>垃圾分类宣传周</h3><p>海棠溪社区将于3月16日-22日开展垃圾分类宣传周活动。</p><p>活动内容：垃圾分类知识讲解、互动游戏、积分兑换等。参与活动可领取环保袋、绿植等小礼品。</p>',
          'https://picsum.photos/800/400?random=107',
          '环保工作组',
          '海棠溪社区',
          2,
          102,
          '活动',
          150,
          6,
          '2026-03-13 09:30:00',
          0
      ),
      (
          5,
          '海棠溪社区春季健步走活动',
          '3月19日沿江步道健步走，全程5公里，欢迎居民报名。',
          '<h3>春季健步走</h3><p>海棠溪社区将于3月19日举办春季健步走活动，全程5公里，沿江步道风景优美。</p><p>集合时间：上午8:30。集合地点：海棠溪广场。报名方式：联系各楼栋长或扫码报名。</p>',
          'https://picsum.photos/800/400?random=108',
          '文体组',
          '海棠溪社区',
          2,
          102,
          '运动',
          95,
          4,
          '2026-03-12 14:15:00',
          0
      );

-- =====================================================
-- 6. 添加外键约束
-- =====================================================
ALTER TABLE `info_article`
    ADD CONSTRAINT `fk_info_article_category`
        FOREIGN KEY (`categoryId`) REFERENCES `info_category` (`id`) ON DELETE CASCADE;
