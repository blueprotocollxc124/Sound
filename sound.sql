/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : sound

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 21/03/2022 17:55:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `class_id` bigint(20) NOT NULL COMMENT '班级id',
  `class_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '班级名称',
  PRIMARY KEY (`class_id`) USING BTREE,
  UNIQUE INDEX `class_id`(`class_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `course_id` bigint(20) NOT NULL COMMENT '课程号',
  `course_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程名称',
  `course_student` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程学生',
  `head_addr` varbinary(255) NULL DEFAULT NULL COMMENT '课程图片路径',
  PRIMARY KEY (`course_id`) USING BTREE,
  UNIQUE INDEX `course_id`(`course_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_teacher
-- ----------------------------
DROP TABLE IF EXISTS `course_teacher`;
CREATE TABLE `course_teacher`  (
  `course_id` bigint(20) NULL DEFAULT NULL,
  `teacher_id` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_estonian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_work
-- ----------------------------
DROP TABLE IF EXISTS `course_work`;
CREATE TABLE `course_work`  (
  `course_id` bigint(20) NULL DEFAULT NULL,
  `work_id` bigint(20) NOT NULL,
  `name` varbinary(255) NULL DEFAULT NULL,
  `work_describe` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `picture_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`work_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for email
-- ----------------------------
DROP TABLE IF EXISTS `email`;
CREATE TABLE `email`  (
  `email_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `teacher_id` bigint(11) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `picture_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`email_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exec
-- ----------------------------
DROP TABLE IF EXISTS `exec`;
CREATE TABLE `exec`  (
  `exec_id` bigint(11) NOT NULL COMMENT '练习id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '练习名',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '练习内容',
  `picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '练习的图片的位置',
  PRIMARY KEY (`exec_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `stu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '学生id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `signature` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个性签名',
  `sex` int(11) NULL DEFAULT NULL COMMENT '性别',
  `head_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '\"\"' COMMENT '头像地址',
  `group_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织码',
  `email` varbinary(255) NULL DEFAULT NULL COMMENT '邮箱',
  `perms` varbinary(255) NULL DEFAULT 'perms[student]' COMMENT '权限',
  `open_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`phone`) USING BTREE,
  UNIQUE INDEX `stu_id`(`stu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for student_course
-- ----------------------------
DROP TABLE IF EXISTS `student_course`;
CREATE TABLE `student_course`  (
  `stu_id` bigint(20) NOT NULL COMMENT '学生id',
  `stu_courses` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  PRIMARY KEY (`stu_id`, `stu_courses`) USING BTREE,
  UNIQUE INDEX `stu_id`(`stu_id`, `stu_courses`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for student_email
-- ----------------------------
DROP TABLE IF EXISTS `student_email`;
CREATE TABLE `student_email`  (
  `stu_id` bigint(11) NULL DEFAULT NULL COMMENT '学生id',
  `email_id` bigint(11) NULL DEFAULT NULL COMMENT '邮件id',
  `statue` int(11) NULL DEFAULT 0 COMMENT '邮件状态，0表示未读'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for student_exec
-- ----------------------------
DROP TABLE IF EXISTS `student_exec`;
CREATE TABLE `student_exec`  (
  `student_id` bigint(11) NULL DEFAULT NULL COMMENT '学生id',
  `exec_id` bigint(11) NULL DEFAULT NULL COMMENT '练习id',
  `code` int(11) NULL DEFAULT NULL COMMENT '类型',
  `score` int(11) NULL DEFAULT NULL COMMENT '学生练习的分数',
  `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `source_file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for student_work
-- ----------------------------
DROP TABLE IF EXISTS `student_work`;
CREATE TABLE `student_work`  (
  `stu_id` bigint(11) NULL DEFAULT NULL COMMENT '学生id',
  `course_id` bigint(11) NULL DEFAULT NULL COMMENT '课程id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_estonian_ci NULL DEFAULT NULL COMMENT '作业名称',
  `work_id` bigint(11) NULL DEFAULT NULL COMMENT '作业id',
  `status` int(11) NULL DEFAULT NULL COMMENT '是否完成作业0未完成',
  `score` int(11) NULL DEFAULT NULL COMMENT '分数',
  `teacher_comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_estonian_ci NULL DEFAULT NULL COMMENT '教师评语',
  `picture_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_estonian_ci NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_estonian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `tea_id` bigint(20) NOT NULL COMMENT '教师id',
  `teachername` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '教师名称',
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机',
  PRIMARY KEY (`tea_id`) USING BTREE,
  UNIQUE INDEX `tea_id`(`tea_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
