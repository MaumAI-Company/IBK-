


-- -----------------------------
-- maummcc 스키마
-- 초기 배포용
-- 버전 2.0.0
-- 전체 내용(DDL + 데이터 + 함수) 포함
-- 생성 날짜: 2025-10-13
-- root 권한으로 실행 필요
-- -----------------------------


-- -----------------------------
-- 스키마 생성 및 사용자 권한 부여
-- -----------------------------

-- 1. 스키마 생성 (없으면 생성)
CREATE DATABASE IF NOT EXISTS `maummcc_test` 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_general_ci;

-- 2. 사용자 생성 (없으면 생성)
CREATE USER IF NOT EXISTS 'maummcc'@'%' IDENTIFIED BY 'maum!2024!';

-- 3️. 스키마 권한 부여
GRANT ALL PRIVILEGES ON `maummcc`.* TO 'maummcc'@'%';
FLUSH PRIVILEGES;

-- 4. 사용할 스키마 선택
USE `maummcc_test`;

-- -----------------------------
-- 기존 mysqldump 내용 시작
-- -----------------------------

-- MariaDB dump 10.19  Distrib 10.8.2-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: temp_maummcc
-- ------------------------------------------------------
-- Server version	10.8.2-MariaDB-1:10.8.2+maria~focal-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Sequence structure for `HC_DEPT_SEQ`
--

DROP SEQUENCE IF EXISTS `HC_DEPT_SEQ`;
CREATE SEQUENCE `HC_DEPT_SEQ` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 cache 1000 nocycle ENGINE=InnoDB;
SELECT SETVAL(`HC_DEPT_SEQ`, 1, 0);

--
-- Sequence structure for `HC_MENU_SEQ`
--

DROP SEQUENCE IF EXISTS `HC_MENU_SEQ`;
CREATE SEQUENCE `HC_MENU_SEQ` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 cache 1000 nocycle ENGINE=InnoDB;
SELECT SETVAL(`HC_MENU_SEQ`, 1, 0);

--
-- Sequence structure for `HC_ROLE_SEQ`
--

DROP SEQUENCE IF EXISTS `HC_ROLE_SEQ`;
CREATE SEQUENCE `HC_ROLE_SEQ` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 cache 1000 nocycle ENGINE=InnoDB;
SELECT SETVAL(`HC_ROLE_SEQ`, 1, 0);

--
-- Table structure for table `AI_PRFR_STAT`
--

DROP TABLE IF EXISTS `AI_PRFR_STAT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AI_PRFR_STAT` (
  `STAT_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '통계 ID',
  `TYPE` varchar(20) NOT NULL COMMENT '데이터 유형 (전체, 카드, 세금계산서)',
  `BDGT_PRFR_YMD` varchar(8) NOT NULL COMMENT '예산집행년월일(YYYYMMDD)',
  `BDGT_PRFR_YM` varchar(6) DEFAULT NULL COMMENT '예산집행년월(YYYYMM)',
  `BDGT_PRFR_Y` varchar(4) DEFAULT NULL COMMENT '예산집행년(YYYY)',
  `HDQR_BOB_DCD` char(1) DEFAULT NULL COMMENT '본부/영업점 구분 코드 (1 본부, 2 영업점)',
  `TOTAL` int(11) NOT NULL COMMENT '전체 지급결의 건수',
  `HIT_CNT1` int(11) NOT NULL COMMENT '예산비목정보_적중수',
  `HIT_CNT2` int(11) NOT NULL COMMENT '예산사업ID_적중수',
  `HIT_CNT3` int(11) NOT NULL COMMENT '비목집행사유_적중수',
  `HIT_CNT4` int(11) NOT NULL COMMENT '예산비목정보_예산사업ID_적중수',
  `ARIN_USE_CNT` int(11) NOT NULL COMMENT 'AI 사용 데이터 수',
  `FRRG_TS` datetime NOT NULL DEFAULT current_timestamp() COMMENT '최초등록일시',
  `LSMD_TS` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '최초변경일시',
  PRIMARY KEY (`STAT_ID`),
  UNIQUE KEY `uq_type_bdgt_prfr_ymd_hdqr_bob_dcd` (`TYPE`,`BDGT_PRFR_YMD`,`HDQR_BOB_DCD`)
) ENGINE=InnoDB AUTO_INCREMENT=262 DEFAULT CHARSET=utf8mb3 COMMENT='AI 사용 지급결의 건수 통계 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AI_PRFR_STAT`
--

LOCK TABLES `AI_PRFR_STAT` WRITE;
/*!40000 ALTER TABLE `AI_PRFR_STAT` DISABLE KEYS */;
/*!40000 ALTER TABLE `AI_PRFR_STAT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BATCH_INFERENCE_MODEL`
--

DROP TABLE IF EXISTS `BATCH_INFERENCE_MODEL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BATCH_INFERENCE_MODEL` (
  `TARGET` varchar(20) NOT NULL COMMENT '배치 실행 대상 모델 유형',
  `MODEL_ID` int(11) NOT NULL COMMENT '모델 ID',
  `MOD_ID` varchar(100) DEFAULT NULL COMMENT '수정자 ID',
  `MOD_DT` datetime DEFAULT current_timestamp() COMMENT '수정일시',
  PRIMARY KEY (`TARGET`,`MODEL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='배치 추론 모델 관리 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BATCH_INFERENCE_MODEL`
--

LOCK TABLES `BATCH_INFERENCE_MODEL` WRITE;
/*!40000 ALTER TABLE `BATCH_INFERENCE_MODEL` DISABLE KEYS */;
/*!40000 ALTER TABLE `BATCH_INFERENCE_MODEL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BILL_INPUT`
--

DROP TABLE IF EXISTS `BILL_INPUT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BILL_INPUT` (
  `TXBL_SRN` varchar(25) NOT NULL COMMENT '세금계산서일련번호',
  `BASE_YM` char(6) NOT NULL COMMENT '기준년월',
  `BRCD` char(4) NOT NULL COMMENT '부점코드',
  `ISS_SRN` varchar(25) DEFAULT NULL COMMENT '발행일련번호',
  `HDQR_BOB_DCD` char(1) NOT NULL COMMENT '1 본부, 2 영업점',
  `TXBL_DCD` char(1) NOT NULL COMMENT '세금계산서구분코드 (S 세금계산서, K 계산서)',
  `SPLR_BSNN_NO` varchar(16) DEFAULT NULL COMMENT '공급자사업자번호',
  `SPLR_FRM` varchar(100) DEFAULT NULL COMMENT '공급자상호명',
  `SPLR_BZST_NM` varchar(100) DEFAULT NULL COMMENT '공급자업태명',
  `SPLR_ITMS_NM` varchar(100) DEFAULT NULL COMMENT '공급자종목명',
  `ISS_AMT` int(18) NOT NULL COMMENT '발행금액',
  `TXBL_LSAR_NM` varchar(150) DEFAULT NULL COMMENT '세금계산서품목명',
  `RCV_YMD` varchar(8) NOT NULL DEFAULT date_format(current_timestamp(),'%Y%m%d') COMMENT '수신년월일',
  `JOB_YMD` varchar(8) DEFAULT NULL COMMENT '작업년월일',
  `JOB_YN` char(1) DEFAULT 'N' COMMENT '작업여부',
  `FRRG_TS` datetime NOT NULL DEFAULT current_timestamp() COMMENT '최초등록일시',
  `FRRG_EMN` char(6) NOT NULL DEFAULT 'BATCH' COMMENT '최초등록번호',
  `LSMD_TS` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '최초변경일시',
  `LSMD_EMN` char(6) NOT NULL DEFAULT 'BATCH' COMMENT '최종변경직원번호',
  `NO` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`TXBL_SRN`,`BASE_YM`,`BRCD`),
  UNIQUE KEY `NO` (`NO`)
) ENGINE=InnoDB AUTO_INCREMENT=1000314 DEFAULT CHARSET=utf8mb3 COMMENT=' AI 추론을 위해 주입할 세금명세서 정보 데이터를 담는 테이블 ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BILL_INPUT`
--

LOCK TABLES `BILL_INPUT` WRITE;
/*!40000 ALTER TABLE `BILL_INPUT` DISABLE KEYS */;
/*!40000 ALTER TABLE `BILL_INPUT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BILL_LEARNING_DATA`
--

DROP TABLE IF EXISTS `BILL_LEARNING_DATA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BILL_LEARNING_DATA` (
  `BRCD` char(4) NOT NULL COMMENT '부점코드',
  `BDGT_PRFR_YM` char(6) NOT NULL COMMENT '예산집행년월',
  `BDGT_PRFR_NO` varchar(7) NOT NULL COMMENT '예산집행번호',
  `BDGT_PRFR_YMD` varchar(8) NOT NULL COMMENT '집행년월일',
  `BASE_YM` char(6) NOT NULL COMMENT '기준년월',
  `TXBL_SRN` varchar(25) NOT NULL COMMENT '세금계산서일련번호',
  `HDQR_BOB_DCD` char(1) NOT NULL COMMENT '1 본부, 2 영업점',
  `TXBL_DCD` char(1) NOT NULL COMMENT '세금계산서구분코드 (S 세금계산서, K 계산서)',
  `SPLR_BSNN_NO` varchar(16) DEFAULT NULL COMMENT '공급자사업자번호',
  `SPLR_FRM` varchar(100) DEFAULT NULL COMMENT '공급자상호명',
  `SPLR_BZST_NM` varchar(100) DEFAULT NULL COMMENT '공급자업태명',
  `SPLR_ITMS_NM` varchar(100) DEFAULT NULL COMMENT '공급자종목명',
  `ISS_AMT` int(18) NOT NULL COMMENT '발행금액',
  `TXBL_LSAR_NM` varchar(150) DEFAULT NULL COMMENT '세금계산서품목명',
  `BDMN_ITEX_MNGM_NO` varchar(8) NOT NULL COMMENT '예산관리비목관리번호(비목코드)',
  `BDGT_BSNS_FRCS_CON` varchar(100) NOT NULL COMMENT '예산사업ID내용(사업-세부사업)',
  `BDGT_PRFR_RSN_FRCS_CON` varchar(100) NOT NULL COMMENT '비목집행사유내용(비목-집행사유코드)',
  `RCV_YMD` varchar(8) DEFAULT date_format(current_timestamp(),'%Y%m%d') COMMENT '수신년월일',
  `JOB_YMD` varchar(8) DEFAULT NULL COMMENT '작업년월일',
  `JOB_YN` char(1) DEFAULT 'N' COMMENT '작업여부',
  `FRRG_TS` datetime DEFAULT current_timestamp() COMMENT '최초등록일시',
  `FRRG_EMN` char(6) NOT NULL DEFAULT 'BATCH' COMMENT '최초등록번호',
  `LSMD_TS` datetime DEFAULT current_timestamp() COMMENT '최종변경일시',
  `LSMD_EMN` char(6) NOT NULL DEFAULT 'BATCH' COMMENT '최종변경직원번호',
  `ISS_SRN` varchar(25) DEFAULT NULL COMMENT '발행일련번호',
  `BDGT_EXNS_PAMT_MCD` char(2) DEFAULT NULL COMMENT '예산경비지급방법코드',
  `ACIM_CON` varchar(300) DEFAULT NULL COMMENT '계좌정보내용',
  `ARIF_MDL_USE_YN` char(1) DEFAULT NULL COMMENT 'AI 사용 여부',
  PRIMARY KEY (`BRCD`,`BDGT_PRFR_YM`,`BDGT_PRFR_NO`),
  KEY `IDX_RCV_YMD` (`RCV_YMD`),
  KEY `IDX_TXBL_SRN` (`TXBL_SRN`),
  KEY `IDX_TXBL_SRN_BASE_YM_BRCD` (`TXBL_SRN`,`BASE_YM`,`BRCD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='세금계산서 내역을 토대로 AI 모델 생성을 위한 학습데이터를 담을 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BILL_LEARNING_DATA`
--

LOCK TABLES `BILL_LEARNING_DATA` WRITE;
/*!40000 ALTER TABLE `BILL_LEARNING_DATA` DISABLE KEYS */;
/*!40000 ALTER TABLE `BILL_LEARNING_DATA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BILL_OUTPUT`
--

DROP TABLE IF EXISTS `BILL_OUTPUT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BILL_OUTPUT` (
  `TXBL_SRN` varchar(25) NOT NULL COMMENT '세금계산서일련번호',
  `BASE_YM` char(6) NOT NULL COMMENT '기준년월',
  `BRCD` char(4) NOT NULL COMMENT '부점코드',
  `ISS_SRN` varchar(25) DEFAULT NULL COMMENT '발행일련번호',
  `HDQR_BOB_DCD` char(1) NOT NULL COMMENT '1 본부, 2 영업점',
  `BDGT_ITEX_FRCS_CON` varchar(100) NOT NULL COMMENT '예산비목정보내용 (비목코드), 3순위까지 구분자 (|)',
  `BDGT_BSNS_FRCS_CON` varchar(100) NOT NULL COMMENT '예산사업ID내용 (부점코드-사업-세부사업), 3순위까지 구분자 (|)',
  `BDGT_PRFR_RSN_FRCS_CON` varchar(100) NOT NULL COMMENT '비목집행사유내용 (비목-집행사유코드), 3순위까지 구분자 (|)',
  `BDGT_EXNS_PAMT_MCD` char(10) DEFAULT NULL COMMENT '예산경비지급방법코드',
  `ACIM_CON` varchar(300) DEFAULT NULL COMMENT '계좌정보내용',
  `BDGT_ITEX_FRCS_PRB_CON` varchar(100) NOT NULL COMMENT '예산비목예측확률내용',
  `BDGT_BSNS_FRCS_PRB_CON` varchar(100) NOT NULL COMMENT '예산사업예측확률내용',
  `BDGT_PRFR_RSN_FRCS_PRB_CON` varchar(100) NOT NULL COMMENT '예산집행사유예측확률내용',
  `BDGT_EXNS_PAMT_MCD_PRB_CON` varchar(100) NOT NULL COMMENT '예산경비지급방법코드예측확률내용',
  `ACIM_PRB_CON` varchar(100) NOT NULL COMMENT '계좌정보예측확률내용',
  `RSRE_YMD` varchar(8) NOT NULL COMMENT '결과등록년월일',
  `FRRG_TS` datetime DEFAULT current_timestamp() COMMENT '최초등록일시',
  `FRRG_EMN` char(6) DEFAULT NULL COMMENT '최초등록직원번호',
  `LSMD_TS` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '최종변경일시',
  `LSMD_EMN` char(6) DEFAULT NULL COMMENT '최종변경직원번호',
  `LEARNING_MODEL_ID` int(11) NOT NULL COMMENT '추론에 사용한 모델 ID',
  `NO` int(11) NOT NULL AUTO_INCREMENT COMMENT '순서번호 (자동증가)',
  `BDGT_ITEX_FRCS_CON_HIT_YN` char(1) DEFAULT NULL COMMENT '예산비목정보내용 (비목코드) 적중여부',
  `BDGT_BSNS_FRCS_CON_HIT_YN` char(1) DEFAULT NULL COMMENT '예산사업ID내용 (부점코드-사업-세부사업) 적중여부',
  `BDGT_PRFR_RSN_FRCS_CON_HIT_YN` char(1) DEFAULT NULL COMMENT '비목집행사유내용 (비목-집행사유코드) 적중여부',
  `RSRE_Y` varchar(4) DEFAULT NULL COMMENT '결과등록년',
  `RSRE_YM` varchar(6) DEFAULT NULL COMMENT '결과등록년월',
  PRIMARY KEY (`TXBL_SRN`,`BASE_YM`,`BRCD`),
  UNIQUE KEY `NO_UNIQUE` (`NO`),
  KEY `IDX_RSRE_YMD` (`RSRE_YMD`),
  KEY `IDX_RSRE_Y` (`RSRE_Y`),
  KEY `IDX_RSRE_YM` (`RSRE_YM`)
) ENGINE=InnoDB AUTO_INCREMENT=10372 DEFAULT CHARSET=utf8mb3 COMMENT='세금계산서 내역을 토대로 AI 추론하여 결과로 받은 지급결의 작성 추천 데이터를 담고 있는 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BILL_OUTPUT`
--

LOCK TABLES `BILL_OUTPUT` WRITE;
/*!40000 ALTER TABLE `BILL_OUTPUT` DISABLE KEYS */;
/*!40000 ALTER TABLE `BILL_OUTPUT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CARD_INPUT`
--

DROP TABLE IF EXISTS `CARD_INPUT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CARD_INPUT` (
  `TSTM_YMD` varchar(8) NOT NULL COMMENT '카드 증빙년월일',
  `TSTM_NO` varchar(20) NOT NULL COMMENT '카드 증빙번호',
  `BRCD` char(4) NOT NULL COMMENT '부점코드',
  `HDQR_BOB_DCD` char(1) NOT NULL COMMENT '1 본부, 2 영업점',
  `CDN` varchar(16) NOT NULL COMMENT '카드번호',
  `BDGT_TSTM_USE_HMS` char(2) NOT NULL COMMENT '예산증빙사용시각',
  `AFST_NM` varchar(50) NOT NULL COMMENT '가맹점명',
  `BZDY_YN` char(1) NOT NULL COMMENT '영업일여부',
  `AMSL_AMT` int(18) NOT NULL COMMENT '매출금액',
  `TPBS_NM` varchar(100) NOT NULL COMMENT '업종명',
  `AFST_BZN` char(10) DEFAULT NULL COMMENT '가맹점사업자등록번호',
  `AMSL_AFST_NO` varchar(12) DEFAULT NULL COMMENT '매출가맹점번호',
  `AFST_TPBCD` char(4) DEFAULT NULL COMMENT '가맹점업종코드',
  `AFST_DTL_ADR` varchar(200) DEFAULT NULL COMMENT '가맹점상세주소',
  `BRNC_ADR` varchar(100) DEFAULT NULL COMMENT '부점주소',
  `RCV_YMD` varchar(8) NOT NULL DEFAULT date_format(current_timestamp(),'%Y%m%d') COMMENT '수신년월일',
  `JOB_YMD` varchar(8) DEFAULT NULL COMMENT '작업년월일',
  `JOB_YN` char(1) DEFAULT 'N' COMMENT '작업여부',
  `FRRG_TS` datetime NOT NULL DEFAULT current_timestamp() COMMENT '최초등록일시',
  `FRRG_EMN` char(6) NOT NULL DEFAULT 'BATCH' COMMENT '최초등록번호',
  `LSMD_TS` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '최종변경일시',
  `LSMD_EMN` char(6) NOT NULL DEFAULT 'BATCH' COMMENT '최종변경직원번호',
  `NO` int(11) NOT NULL AUTO_INCREMENT COMMENT '순서번호 (자동증가)',
  PRIMARY KEY (`TSTM_YMD`,`TSTM_NO`,`BRCD`),
  UNIQUE KEY `NO` (`NO`)
) ENGINE=InnoDB AUTO_INCREMENT=4926107 DEFAULT CHARSET=utf8mb3 COMMENT=' AI 추론을 위해 주입할 카드 증빙 정보 데이터를 담는 테이블 ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CARD_INPUT`
--

LOCK TABLES `CARD_INPUT` WRITE;
/*!40000 ALTER TABLE `CARD_INPUT` DISABLE KEYS */;
/*!40000 ALTER TABLE `CARD_INPUT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CARD_LEARNING_DATA`
--

DROP TABLE IF EXISTS `CARD_LEARNING_DATA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CARD_LEARNING_DATA` (
  `BRCD` char(4) NOT NULL COMMENT '부점코드',
  `BDGT_PRFR_YM` char(6) NOT NULL COMMENT '예산집행년월',
  `BDGT_PRFR_NO` varchar(7) NOT NULL COMMENT '예산집행번호',
  `BDGT_PRFR_YMD` varchar(8) NOT NULL DEFAULT '' COMMENT '예산집행년월일',
  `TSTM_YMD` varchar(8) DEFAULT NULL COMMENT '카드 증빙년월일',
  `TSTM_NO` varchar(20) DEFAULT NULL COMMENT '카드 증빙번호',
  `HDQR_BOB_DCD` char(1) NOT NULL COMMENT '1 본부, 2 영업점',
  `CDN` varchar(16) NOT NULL COMMENT '카드번호',
  `BDGT_TSTM_USE_HMS` char(2) NOT NULL COMMENT '예산증빙사용시각',
  `AFST_NM` varchar(50) NOT NULL COMMENT '가맹점명',
  `BZDY_YN` char(1) NOT NULL COMMENT '영업일여부',
  `AMSL_AMT` int(18) NOT NULL COMMENT '매출금액',
  `TPBS_NM` varchar(100) NOT NULL COMMENT '업종명',
  `AFST_BZN` char(10) DEFAULT NULL COMMENT '가맹점사업자등록번호',
  `AMSL_AFST_NO` varchar(12) DEFAULT NULL COMMENT '매출가맹점번호',
  `AFST_TPBCD` char(4) DEFAULT NULL COMMENT '가맹점업종코드',
  `AFST_DTL_ADR` varchar(200) DEFAULT NULL COMMENT '가맹점상세주소',
  `BRNC_ADR` varchar(100) DEFAULT NULL COMMENT '부점주소',
  `BDMN_ITEX_MNGM_NO` varchar(8) DEFAULT NULL COMMENT '예산관리비목관리번호(비목코드)',
  `BDGT_BSNS_FRCS_CON` varchar(100) DEFAULT NULL COMMENT '예산사업ID내용(사업-세부사업)',
  `BDGT_PRFR_RSN_FRCS_CON` varchar(100) DEFAULT NULL COMMENT '비목집행사유내용(비목-집행사유코드)',
  `RCV_YMD` varchar(8) NOT NULL DEFAULT date_format(current_timestamp(),'%Y%m%d') COMMENT '수신년월일',
  `JOB_YMD` varchar(8) DEFAULT NULL COMMENT '작업년월일',
  `JOB_YN` char(1) DEFAULT 'N' COMMENT '작업여부',
  `FRRG_TS` datetime NOT NULL DEFAULT current_timestamp() COMMENT '최초등록일시',
  `FRRG_EMN` char(6) NOT NULL DEFAULT 'BATCH' COMMENT '최초등록직원번호',
  `LSMD_TS` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '최종변경일시',
  `LSMD_EMN` char(6) NOT NULL DEFAULT 'BATCH' COMMENT '최종변경직원번호',
  `ARIF_MDL_USE_YN` char(1) DEFAULT NULL COMMENT 'AI 사용 여부',
  PRIMARY KEY (`BRCD`,`BDGT_PRFR_YM`,`BDGT_PRFR_NO`),
  KEY `IDX_RCV_YMD` (`RCV_YMD`),
  KEY `IDX_TSTM_YMD_TSTM_NO` (`TSTM_YMD`,`TSTM_NO`),
  KEY `IDX_TSTM_YMD_TSTM_NO_BRCD` (`TSTM_YMD`,`TSTM_NO`,`BRCD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='카드 증빙 내역을 토대로 AI 모델 생성을 위한 학습데이터를 담을 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CARD_LEARNING_DATA`
--

LOCK TABLES `CARD_LEARNING_DATA` WRITE;
/*!40000 ALTER TABLE `CARD_LEARNING_DATA` DISABLE KEYS */;
/*!40000 ALTER TABLE `CARD_LEARNING_DATA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CARD_OUTPUT`
--

DROP TABLE IF EXISTS `CARD_OUTPUT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CARD_OUTPUT` (
  `TSTM_YMD` varchar(8) NOT NULL COMMENT '카드 증빙년월일',
  `TSTM_NO` varchar(20) NOT NULL COMMENT '카드 증빙번호',
  `BRCD` char(4) NOT NULL COMMENT '부점코드',
  `HDQR_BOB_DCD` char(1) NOT NULL COMMENT '1 본부, 2 영업점',
  `BDGT_ITEX_FRCS_CON` varchar(100) NOT NULL COMMENT '예산비목정보내용 (비목코드), 3순위까지 구분자 (|)',
  `BDGT_BSNS_FRCS_CON` varchar(100) NOT NULL COMMENT '예산사업ID내용 (부점코드-사업-세부사업), 3순위까지 구분자 (|)',
  `BDGT_PRFR_RSN_FRCS_CON` varchar(100) NOT NULL COMMENT '비목집행사유내용 (비목-집행사유코드), 3순위까지 구분자 (|)',
  `BDGT_PRFR_RSN_FRCS_PRB_CON` varchar(100) NOT NULL COMMENT '예산집행사유예측확률내용',
  `BDGT_BSNS_FRCS_PRB_CON` varchar(100) NOT NULL COMMENT '예산사업예측확률내용',
  `BDGT_ITEX_FRCS_PRB_CON` varchar(100) NOT NULL COMMENT '예산비목예측확률내용',
  `RSRE_YMD` varchar(8) NOT NULL COMMENT '결과등록년월일',
  `FRRG_TS` datetime NOT NULL DEFAULT current_timestamp() COMMENT '최초등록일시',
  `FRRG_EMN` char(6) DEFAULT NULL COMMENT '최초등록직원번호',
  `LSMD_TS` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '최종변경일시',
  `LSMD_EMN` char(6) DEFAULT NULL COMMENT '최종변경직원번호',
  `LEARNING_MODEL_ID` int(11) NOT NULL COMMENT '추론에 사용한 모델 ID',
  `NO` int(11) NOT NULL AUTO_INCREMENT COMMENT '순서번호 (자동증가)',
  `BDGT_ITEX_FRCS_CON_HIT_YN` char(1) DEFAULT NULL COMMENT '예산비목정보내용 (비목코드) 적중여부',
  `BDGT_BSNS_FRCS_CON_HIT_YN` char(1) DEFAULT NULL COMMENT '예산사업ID내용 (부점코드-사업-세부사업) 적중여부',
  `BDGT_PRFR_RSN_FRCS_CON_HIT_YN` char(1) DEFAULT NULL COMMENT '비목집행사유내용 (비목-집행사유코드) 적중여부',
  `RSRE_Y` varchar(4) DEFAULT NULL COMMENT '결과등록년',
  `RSRE_YM` varchar(6) DEFAULT NULL COMMENT '결과등록년월',
  PRIMARY KEY (`TSTM_YMD`,`TSTM_NO`,`BRCD`),
  UNIQUE KEY `NO` (`NO`),
  KEY `IDX_RSRE_YMD` (`RSRE_YMD`),
  KEY `IDX_RSRE_Y` (`RSRE_Y`),
  KEY `IDX_RSRE_YM` (`RSRE_YM`)
) ENGINE=InnoDB AUTO_INCREMENT=428282 DEFAULT CHARSET=utf8mb3 COMMENT='카드 증빙 내역을 토대로 AI 추론하여 결과로 받은 지급결의 작성 추천 데이터를 담고 있는 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CARD_OUTPUT`
--

LOCK TABLES `CARD_OUTPUT` WRITE;
/*!40000 ALTER TABLE `CARD_OUTPUT` DISABLE KEYS */;
/*!40000 ALTER TABLE `CARD_OUTPUT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DEPLOY_HISTORY`
--

DROP TABLE IF EXISTS `DEPLOY_HISTORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DEPLOY_HISTORY` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MODEL_ID` int(11) NOT NULL COMMENT '모델 ID',
  `EXEC_ID` varchar(100) DEFAULT NULL COMMENT '실행자 ID',
  `EXEC_DT` datetime DEFAULT current_timestamp() COMMENT '실행일시',
  `RESULT` varchar(20) DEFAULT NULL COMMENT '배포 실행 결과',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COMMENT='배포 이력 관리 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DEPLOY_HISTORY`
--

LOCK TABLES `DEPLOY_HISTORY` WRITE;
/*!40000 ALTER TABLE `DEPLOY_HISTORY` DISABLE KEYS */;
/*!40000 ALTER TABLE `DEPLOY_HISTORY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HC_DEPT`
--

DROP TABLE IF EXISTS `HC_DEPT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HC_DEPT` (
  `DEPT_SEQ` int(11) NOT NULL,
  `PAR_ID` varchar(100) NOT NULL COMMENT '상위부서아이디',
  `DEPT_ID` varchar(100) NOT NULL COMMENT '부서아이디',
  `DEPT_CODE` varchar(100) NOT NULL COMMENT '부서코드',
  `DEPT_NAME` varchar(100) NOT NULL COMMENT '부서명',
  `DEPT_ENG_NAME` varchar(100) DEFAULT NULL COMMENT '부서명(영문)',
  `DEPT_DEPTH` int(5) unsigned DEFAULT 0 COMMENT '노드 깊이',
  `DEPT_ORDER` int(5) unsigned DEFAULT 0 COMMENT '노드 순서',
  `DEPT_STAT` varchar(20) DEFAULT 'use' COMMENT 'use, unused, delete 등 상태',
  `DEPT_REG_ID` varchar(100) NOT NULL COMMENT '등록자',
  `DEPT_REG_DT` datetime NOT NULL COMMENT '등록일시',
  `DEPT_MOD_ID` varchar(100) NOT NULL COMMENT '변경자',
  `DEPT_MOD_DT` datetime NOT NULL COMMENT '변경일시',
  `DEPT_COLUMN1` varchar(200) DEFAULT NULL COMMENT '컬럼1',
  `DEPT_COLUMN2` varchar(200) DEFAULT NULL COMMENT '컬럼2',
  `DEPT_COLUMN3` varchar(200) DEFAULT NULL COMMENT '컬럼3',
  `DEPT_COLUMN4` varchar(200) DEFAULT NULL COMMENT '컬럼4',
  `DEPT_COLUMN5` varchar(200) DEFAULT NULL COMMENT '컬럼5',
  UNIQUE KEY `DEPT_ID` (`DEPT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='HC_DEPT';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HC_DEPT`
--

LOCK TABLES `HC_DEPT` WRITE;
/*!40000 ALTER TABLE `HC_DEPT` DISABLE KEYS */;
INSERT INTO `HC_DEPT` VALUES
(1,'system','ID00001','D10000','IT팀',NULL,0,0,'use','','2023-01-11 14:01:38','','2023-01-11 14:01:38',NULL,NULL,NULL,NULL,NULL),
(2,'system','ID00002','D20000','심사팀','',0,0,'use','','2023-01-11 14:01:38','admin','2025-02-21 09:22:49',NULL,NULL,NULL,NULL,NULL),
(3,'system','ID00003','D30000','서비스관리',NULL,0,0,'use','','2023-01-11 14:01:38','','2023-01-11 14:01:38',NULL,NULL,NULL,NULL,NULL),
(4,'system','ID00004','D40000','준법감시팀','',0,0,'use','','2023-01-11 14:01:38','admin','2024-06-17 15:10:32',NULL,NULL,NULL,NULL,NULL),
(5,'system','ID00021','D50000','예산운용팀','Budget Management Team',0,0,'use','admin','2024-06-17 15:09:58','admin','2024-06-17 15:10:43',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `HC_DEPT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HC_MEMBER`
--

DROP TABLE IF EXISTS `HC_MEMBER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HC_MEMBER` (
  `MEM_SEQ` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '회원일련번호',
  `DEPT_ID` varchar(100) NOT NULL COMMENT '부서아이디',
  `MEM_ID` varchar(100) NOT NULL COMMENT '회원아이디',
  `MEM_NAME` varchar(50) NOT NULL COMMENT '회원명',
  `MEM_TYPE` varchar(100) DEFAULT NULL COMMENT 'OAUTH 연동시 사용',
  `MEM_STAT` varchar(20) NOT NULL DEFAULT 'use' COMMENT 'use, unused, delete 등 상태',
  `MEM_BEFO_PWD` varchar(255) DEFAULT NULL COMMENT '이전 비밀번호',
  `MEM_PWD` varchar(255) NOT NULL COMMENT '현재 비밀번호',
  `MEM_GENDER` char(1) DEFAULT NULL COMMENT 'M: 남자, W:여자',
  `MEM_BIRTHDAY` varchar(10) DEFAULT NULL COMMENT 'YYYYMMDD 8자리',
  `MEM_PHONE` varchar(20) DEFAULT NULL COMMENT '010-0000-0000 13자리',
  `MEM_RANK` varchar(20) DEFAULT NULL COMMENT '직급',
  `MEM_ZIPCODE` varchar(20) DEFAULT NULL COMMENT '우편번호',
  `MEM_ADDR` varchar(200) DEFAULT NULL COMMENT '주소',
  `MEM_ADDR_DETAIL` varchar(200) DEFAULT NULL COMMENT '상세 주소',
  `MEM_EMAIL` varchar(100) DEFAULT NULL COMMENT '이메일',
  `MEM_REG_ID` varchar(100) NOT NULL COMMENT '등록자',
  `MEM_REG_DT` datetime NOT NULL COMMENT '등록일시',
  `MEM_MOD_ID` varchar(100) NOT NULL COMMENT '변경자',
  `MEM_MOD_DT` datetime NOT NULL COMMENT '변경일시',
  `MEM_LST_DT` datetime DEFAULT NULL COMMENT '마지막접속일자',
  `MEM_FAIL_CNT` int(11) unsigned DEFAULT NULL COMMENT '비밀번호실패횟수',
  `MEM_FAIL_DT` datetime DEFAULT NULL COMMENT '비밀번호실패일자',
  `MEM_COLUMN1` varchar(200) DEFAULT NULL COMMENT '컬럼1',
  `MEM_COLUMN2` varchar(200) DEFAULT NULL COMMENT '컬럼2',
  `MEM_COLUMN3` varchar(200) DEFAULT NULL COMMENT '컬럼3',
  `MEM_COLUMN4` varchar(200) DEFAULT NULL COMMENT '컬럼4',
  `MEM_COLUMN5` varchar(200) DEFAULT NULL COMMENT '컬럼5',
  `MEM_SNO` varchar(100) DEFAULT NULL COMMENT '사번',
  `RECV_YN` varchar(100) DEFAULT 'N' COMMENT '장애발생수신여부',
  `SND_YN` varchar(100) DEFAULT 'N' COMMENT '장애발생발신여부',
  PRIMARY KEY (`MEM_SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COMMENT='HC_MEMBER';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HC_MEMBER`
--

LOCK TABLES `HC_MEMBER` WRITE;
/*!40000 ALTER TABLE `HC_MEMBER` DISABLE KEYS */;
INSERT INTO `HC_MEMBER` VALUES
(1,'system','admin','관리자','ADMIN','use',NULL,'{bcrypt}$2a$10$2FuVUHE9xJMN0VbsYxgk2udQ7S4P8g05FXvl6NGB0VHxYrl65zRGW','M','','',NULL,NULL,NULL,NULL,'test@test.kr','admin','2023-01-11 14:01:38','admin','2025-02-13 10:10:41','2023-01-11 14:01:38',0,'2024-11-20 10:17:37','','N','SUPER',NULL,NULL,'A99442','Y','Y'),
(2,'system','user','사용자','USER','use','{bcrypt}$2a$10$hlirXDid7dO/7yM/1QTpC.O/472QoTBhi24glhbul3Zk8DPLKEX4e','{bcrypt}$2a$10$hlirXDid7dO/7yM/1QTpC.O/472QoTBhi24glhbul3Zk8DPLKEX4e','M','','',NULL,NULL,NULL,NULL,'','admin','2023-01-11 14:01:38','admin','2025-02-28 13:47:44','2023-01-11 14:01:38',0,'2023-04-06 12:01:05','','N',NULL,NULL,NULL,'042353','Y','N');
/*!40000 ALTER TABLE `HC_MEMBER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HC_MEMBER_MENU`
--

DROP TABLE IF EXISTS `HC_MEMBER_MENU`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HC_MEMBER_MENU` (
  `MEM_SEQ` bigint(20) NOT NULL COMMENT '회원일련번호',
  `MEM_ID` varchar(100) NOT NULL COMMENT '회원아이디',
  `MENU_ID` varchar(100) NOT NULL COMMENT '메뉴아이디',
  `ROLE_ID` varchar(100) DEFAULT NULL COMMENT '역할아이디',
  `MENU_USE` varchar(10) DEFAULT 'Y' COMMENT '사용여부',
  `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록일자',
  `REG_ID` varchar(100) DEFAULT NULL COMMENT '등록자 ID',
  `MOD_DT` datetime DEFAULT current_timestamp() COMMENT '수정일자',
  `MOD_ID` varchar(100) DEFAULT NULL COMMENT '수정자 ID',
  PRIMARY KEY (`MEM_SEQ`,`MENU_ID`),
  KEY `FK_HC_MENU_TO_HC_MEMBER_MENU` (`MENU_ID`),
  CONSTRAINT `FK_HC_MEMBER_TO_HC_MEMBER_MENU` FOREIGN KEY (`MEM_SEQ`) REFERENCES `HC_MEMBER` (`MEM_SEQ`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_HC_MENU_TO_HC_MEMBER_MENU` FOREIGN KEY (`MENU_ID`) REFERENCES `HC_MENU` (`MENU_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='HC_MEMBER_MENU';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HC_MEMBER_MENU`
--

LOCK TABLES `HC_MEMBER_MENU` WRITE;
/*!40000 ALTER TABLE `HC_MEMBER_MENU` DISABLE KEYS */;
INSERT INTO `HC_MEMBER_MENU` VALUES
(1,'admin','ID00001','ALL','Y','2024-09-09 15:46:14','admin','2024-09-09 16:48:29','admin'),
(1,'admin','ID00002','ALL','Y','2024-09-09 15:46:14','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00003','ALL','Y','2024-09-09 15:46:15','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00004','ADMIN','Y','2024-09-09 15:46:15','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00005','ALL','Y','2024-09-09 15:46:14','admin','2024-09-09 16:48:29','admin'),
(1,'admin','ID00006','ALL','Y','2024-09-09 15:46:14','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00007','ALL','Y','2024-09-09 15:46:14','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00008','ALL','Y','2024-09-09 15:46:14','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00009','ALL','Y','2024-09-09 15:46:14','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00010','ALL','Y','2024-09-09 15:46:15','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00011','ALL','Y','2024-09-09 15:46:15','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00012','ADMIN','Y','2024-09-09 15:46:15','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00013','ADMIN','Y','2024-09-09 15:46:15','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00014','ADMIN','Y','2024-09-09 15:46:15','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00015','ADMIN','Y','2024-09-09 15:46:15','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00016','ADMIN','Y','2024-09-09 15:46:15','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00017','ADMIN','Y','2024-09-09 15:46:15','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00018','ALL','Y','2024-09-09 15:46:14','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00019','ALL','Y','2024-09-09 15:46:14','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00020','ALL','Y','2024-09-09 15:46:15','admin','2024-09-09 16:48:30','admin'),
(1,'admin','ID00021','ALL','Y','2024-09-09 15:46:15','admin','2024-09-09 16:48:30','admin');
/*!40000 ALTER TABLE `HC_MEMBER_MENU` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HC_MEMBER_ROLE`
--

DROP TABLE IF EXISTS `HC_MEMBER_ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HC_MEMBER_ROLE` (
  `MEM_SEQ` bigint(20) NOT NULL COMMENT '회원일련번호',
  `MEM_ID` varchar(100) NOT NULL COMMENT '회원아이디',
  `ROLE_ID` varchar(100) NOT NULL COMMENT '역할아이디',
  PRIMARY KEY (`MEM_SEQ`,`ROLE_ID`),
  KEY `FK_HC_ROLE_TO_HC_MEMBER_ROLE` (`ROLE_ID`),
  CONSTRAINT `FK_HC_MEMBER_TO_HC_MEMBER_ROLE` FOREIGN KEY (`MEM_SEQ`) REFERENCES `HC_MEMBER` (`MEM_SEQ`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_HC_ROLE_TO_HC_MEMBER_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `HC_ROLE` (`ROLE_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='HC_MEMBER_ROLE';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HC_MEMBER_ROLE`
--

LOCK TABLES `HC_MEMBER_ROLE` WRITE;
/*!40000 ALTER TABLE `HC_MEMBER_ROLE` DISABLE KEYS */;
INSERT INTO `HC_MEMBER_ROLE` VALUES
(1,'admin','ADMIN'),
(2,'user','USER');
/*!40000 ALTER TABLE `HC_MEMBER_ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HC_MENU`
--

DROP TABLE IF EXISTS `HC_MENU`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HC_MENU` (
  `MENU_SEQ` bigint(20) NOT NULL COMMENT '메뉴일련번호',
  `ROLE_ID` varchar(100) NOT NULL COMMENT '메뉴사용가능한역할',
  `PAR_ID` varchar(100) NOT NULL COMMENT '상위메뉴아이디',
  `MENU_ID` varchar(100) NOT NULL COMMENT '메뉴아이디',
  `MENU_CODE` varchar(100) NOT NULL COMMENT '메뉴코드',
  `MENU_NAME` varchar(100) NOT NULL COMMENT '메뉴명',
  `MENU_ENG_NAME` varchar(100) DEFAULT NULL COMMENT '메뉴명(영문)',
  `MENU_DEPTH` int(5) unsigned DEFAULT 0 COMMENT '노드 깊이',
  `MENU_ORDER` int(5) unsigned DEFAULT 0 COMMENT '노드 순서',
  `MENU_TYPE` varchar(20) DEFAULT 'unused' COMMENT 'unused, page, link, embeded 등 연결 구분',
  `MENU_STAT` varchar(20) DEFAULT 'use' COMMENT 'use, unused, delete 등 상태',
  `MENU_URL` varchar(100) DEFAULT NULL COMMENT '메뉴 URL',
  `MENU_REG_ID` varchar(100) NOT NULL COMMENT '등록자',
  `MENU_REG_DT` datetime NOT NULL COMMENT '등록일시',
  `MENU_MOD_ID` varchar(100) NOT NULL COMMENT '변경자',
  `MENU_MOD_DT` datetime NOT NULL COMMENT '변경일시',
  `MENU_COLUMN1` varchar(200) DEFAULT NULL COMMENT '컬럼1',
  `MENU_COLUMN2` varchar(200) DEFAULT NULL COMMENT '컬럼2',
  `MENU_COLUMN3` varchar(200) DEFAULT NULL COMMENT '컬럼3',
  `MENU_COLUMN4` varchar(200) DEFAULT NULL COMMENT '컬럼4',
  `MENU_COLUMN5` varchar(200) DEFAULT NULL COMMENT '컬럼5',
  PRIMARY KEY (`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='HC_MENU';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HC_MENU`
--

LOCK TABLES `HC_MENU` WRITE;
/*!40000 ALTER TABLE `HC_MENU` DISABLE KEYS */;
INSERT INTO `HC_MENU` VALUES
(1,'ALL','system','ID00001','M10000','지급결의 AI 적용 결과 조회',NULL,1,1,'unused','use','','','2023-01-11 14:01:38','','2023-01-11 14:01:38','ico_chart',NULL,NULL,NULL,NULL),
(2,'ALL','system','ID00002','M20000','학습 관리','',1,2,'unused','use','','','2023-01-11 14:01:38','admin','2024-09-09 16:38:46','ico_database',NULL,NULL,NULL,NULL),
(3,'ALL','system','ID00003','M30000','시스템 상태',NULL,1,3,'unused','use','','','2023-01-11 14:01:38','','2023-01-11 14:01:38','ico_dns',NULL,NULL,NULL,NULL),
(4,'ADMIN','system','ID00004','M40000','Admin','',1,4,'unused','use','','','2023-01-11 14:01:38','admin','2025-02-24 10:35:28','ico_manage',NULL,NULL,NULL,NULL),
(5,'ALL','ID00001','ID00005','M11000','BC카드 지급결의 내역 조회','',2,1,'page','use','/soulGod/report/card','','2023-01-11 14:01:38','admin','2025-02-21 09:29:33',NULL,NULL,NULL,NULL,NULL),
(6,'ALL','ID00001','ID00006','M12000','세금계산서 지급결의 내역 조회','',2,2,'page','use','/soulGod/report/taxInvoice','','2023-01-11 14:01:38','admin','2025-02-21 09:24:50',NULL,NULL,NULL,NULL,NULL),
(7,'ALL','ID00001','ID00007','M13000','기간별 통계',NULL,2,3,'page','use','/soulGod/report/statistic','','2023-01-11 14:01:38','','2023-01-11 14:01:38',NULL,NULL,NULL,NULL,NULL),
(8,'ALL','ID00002','ID00008','M21000','BC카드 학습 데이터 등록','',2,1,'page','use','/soulGod/learn/cardManage','','2023-01-11 14:01:38','admin','2025-03-07 01:03:35',NULL,NULL,NULL,NULL,NULL),
(9,'ALL','ID00002','ID00009','M22000','모델 관리','',2,4,'page','use','/soulGod/learn/modelManage','','2023-01-11 14:01:38','admin','2024-11-29 13:42:23',NULL,NULL,NULL,NULL,NULL),
(10,'ALL','ID00002','ID00010','M23000','배포 관리','',2,5,'page','use','/soulGod/learn/deployManage','','2023-01-11 14:01:38','admin','2024-11-29 13:42:37',NULL,NULL,NULL,NULL,NULL),
(11,'ALL','ID00003','ID00011','M31000','모니터링',NULL,2,1,'page','use','/soulGod/system/monitoring','','2023-01-11 14:01:38','','2023-01-11 14:01:38',NULL,NULL,NULL,NULL,NULL),
(12,'ADMIN','ID00004','ID00012','M41000','조직 관리','',2,1,'unused','use','','','2023-01-11 14:01:38','admin','2025-02-24 10:35:23',NULL,NULL,NULL,NULL,NULL),
(13,'ADMIN','ID00004','ID00013','M42000','메뉴 관리',NULL,2,2,'page','use','/soulGod/admin/menu','','2023-01-11 14:01:38','','2023-01-11 14:01:38',NULL,NULL,NULL,NULL,NULL),
(14,'ADMIN','ID00004','ID00014','M43000','권한 관리',NULL,2,3,'page','use','/soulGod/admin/auth','','2023-01-11 14:01:38','','2023-01-11 14:01:38',NULL,NULL,NULL,NULL,NULL),
(15,'ADMIN','ID00012','ID00015','M41100','부서 관리',NULL,3,1,'unused','use','/soulGod/admin/department','','2023-01-11 14:01:38','','2023-01-11 14:01:38',NULL,NULL,NULL,NULL,NULL),
(16,'ADMIN','ID00012','ID00016','M41200','사용자 관리',NULL,3,2,'unused','use','/soulGod/admin/user','','2023-01-11 14:01:38','','2023-01-11 14:01:38',NULL,NULL,NULL,NULL,NULL),
(17,'ADMIN','ID00004','ID00017','M45000','학습 스케줄러 관리','',2,4,'unused','use','/soulGod/admin/scheduler','admin','2024-11-12 15:11:57','admin','2024-11-12 15:28:09',NULL,NULL,NULL,NULL,NULL),
(18,'ALL','ID00002','ID00018','M24000','학습 템플릿 관리','',2,2,'unused','use','/soulGod/learn/templateManage','admin','2024-11-22 16:52:45','admin','2024-11-29 13:42:09',NULL,NULL,NULL,NULL,NULL),
(19,'ALL','ID00002','ID00019','M25000','학습 데이터 관리','',2,3,'unused','use','/soulGod/learn/learningDataManage','admin','2024-11-29 13:41:39','admin','2024-11-29 13:44:37',NULL,NULL,NULL,NULL,NULL),
(20,'ALL','ID00002','ID00020','M2600','세금계산서 학습 데이터 등록','',2,1,'unused','use','/soulGod/learn/billManage','admin','2025-01-20 13:50:12','admin','2025-01-20 16:15:12',NULL,NULL,NULL,NULL,NULL),
(21,'ALL','ID00002','ID00021','M24000','배포 이력 관리','',2,7,'unused','use','/soulGod/learn/deployHis','admin','2025-06-25 13:03:59','admin','2025-06-26 14:56:42',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `HC_MENU` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HC_ROLE`
--

DROP TABLE IF EXISTS `HC_ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HC_ROLE` (
  `ROLE_SEQ` bigint(20) NOT NULL COMMENT '역할일련번호',
  `ROLE_ID` varchar(100) NOT NULL COMMENT '역할아이디',
  `ROLE_TYPE` varchar(100) NOT NULL COMMENT '역할 유형(로그인, 메뉴)',
  `ROLE_NAME` varchar(50) NOT NULL COMMENT '역할명',
  `ROLE_USE_YN` char(1) NOT NULL COMMENT '사용: Y, 미사용: N',
  `ROLE_REG_ID` varchar(100) NOT NULL COMMENT '등록자',
  `ROLE_REG_DT` datetime NOT NULL COMMENT '등록일시',
  `ROLE_MOD_ID` varchar(100) NOT NULL COMMENT '변경자',
  `ROLE_MOD_DT` datetime NOT NULL COMMENT '변경일시',
  `ROLE_COLUMN1` varchar(200) DEFAULT NULL COMMENT '컬럼1',
  `ROLE_COLUMN2` varchar(200) DEFAULT NULL COMMENT '컬럼2',
  `ROLE_COLUMN3` varchar(200) DEFAULT NULL COMMENT '컬럼3',
  `ROLE_COLUMN4` varchar(200) DEFAULT NULL COMMENT '컬럼4',
  `ROLE_COLUMN5` varchar(200) DEFAULT NULL COMMENT '컬럼5',
  PRIMARY KEY (`ROLE_ID`,`ROLE_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='HC_ROLE';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HC_ROLE`
--

LOCK TABLES `HC_ROLE` WRITE;
/*!40000 ALTER TABLE `HC_ROLE` DISABLE KEYS */;
INSERT INTO `HC_ROLE` VALUES
(1,'ADMIN','login','관리자','Y','admin','2023-01-11 14:01:38','admin','2023-01-11 14:01:38',NULL,NULL,NULL,NULL,NULL),
(3,'EVERY','login','모든사용자','Y','admin','2023-01-11 14:01:38','admin','2023-01-11 14:01:38',NULL,NULL,NULL,NULL,NULL),
(2,'USER','login','사용자','Y','admin','2023-01-11 14:01:38','admin','2023-01-11 14:01:38',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `HC_ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LEARNING_DATA`
--

DROP TABLE IF EXISTS `LEARNING_DATA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LEARNING_DATA` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DATA_NAME` varchar(500) DEFAULT NULL COMMENT '학습데이터명',
  `SELECT_CON` longtext DEFAULT NULL COMMENT '선택조건',
  `HDQR_BOB_DCD` char(1) DEFAULT NULL COMMENT '1 본부, 2 영업점',
  `REG_ID` varchar(100) DEFAULT NULL COMMENT '등록자',
  `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록일자',
  `MOD_ID` varchar(100) DEFAULT NULL COMMENT '수정자',
  `MOD_DT` datetime DEFAULT NULL COMMENT '수정일자',
  `START_DT` datetime DEFAULT NULL COMMENT '시작일자',
  `END_DT` datetime DEFAULT NULL COMMENT '종료일자',
  `TEMPLATE_ID` int(11) DEFAULT NULL COMMENT '템플릿ID',
  `LEARNING_TYPE` varchar(20) DEFAULT NULL COMMENT '학습타입',
  `SCHED_ID` int(11) DEFAULT NULL COMMENT '학습 스케줄러 ID',
  PRIMARY KEY (`ID`),
  KEY `LEARNING_DATA_TEMPLATE_FK` (`TEMPLATE_ID`),
  KEY `LEARNING_DATA_LEARNING_SCHEDULER_FK` (`SCHED_ID`),
  CONSTRAINT `LEARNING_DATA_LEARNING_SCHEDULER_FK` FOREIGN KEY (`SCHED_ID`) REFERENCES `LEARNING_SCHEDULER` (`SCHED_ID`),
  CONSTRAINT `LEARNING_DATA_TEMPLATE_FK` FOREIGN KEY (`TEMPLATE_ID`) REFERENCES `TEMPLATE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=280 DEFAULT CHARSET=utf8mb3 COMMENT='학습데이터관리 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LEARNING_DATA`
--

LOCK TABLES `LEARNING_DATA` WRITE;
/*!40000 ALTER TABLE `LEARNING_DATA` DISABLE KEYS */;
/*!40000 ALTER TABLE `LEARNING_DATA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LEARNING_DATA_INPUT`
--

DROP TABLE IF EXISTS `LEARNING_DATA_INPUT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LEARNING_DATA_INPUT` (
  `COL_NAME` varchar(100) DEFAULT NULL COMMENT '컬럼명',
  `DATA_ID` int(11) DEFAULT NULL COMMENT '학습데이터ID',
  `INOUT_GBN` varchar(20) DEFAULT NULL COMMENT '인/아웃 구분',
  `SNO` int(11) DEFAULT NULL COMMENT '순번',
  KEY `LEARNING_MODEL_INPUT_LEARNING_MODEL_FK` (`DATA_ID`) USING BTREE,
  CONSTRAINT `LEARNING_DATA_INPUT_LEARNING_DATA_FK` FOREIGN KEY (`DATA_ID`) REFERENCES `LEARNING_DATA` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='학습데이터 피처 관리 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LEARNING_DATA_INPUT`
--

LOCK TABLES `LEARNING_DATA_INPUT` WRITE;
/*!40000 ALTER TABLE `LEARNING_DATA_INPUT` DISABLE KEYS */;
/*!40000 ALTER TABLE `LEARNING_DATA_INPUT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LEARNING_MODEL`
--

DROP TABLE IF EXISTS `LEARNING_MODEL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LEARNING_MODEL` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LEARNING_ID` int(11) DEFAULT NULL COMMENT '학습 키',
  `REG_ID` varchar(100) DEFAULT NULL COMMENT '등록자',
  `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록일자',
  `MOD_ID` varchar(100) DEFAULT NULL COMMENT '수정자',
  `MOD_DT` datetime DEFAULT current_timestamp() COMMENT '수정일자',
  `LEARN_NAME` varchar(500) DEFAULT NULL COMMENT '학습 명',
  `EPOCH` varchar(100) DEFAULT NULL COMMENT '학습 데이터 셋을 학습하는 회수',
  `LEARNING_RATE` varchar(100) DEFAULT NULL COMMENT '학습률 , 최적 값을 찾기 위한 기울기의 이동 정도',
  `BATCH_SIZE` varchar(100) DEFAULT NULL COMMENT '학습 데이터셋 중 , 몇 개의 데이터를 묶어서 가중치 값을 갱신할 지 지정하는 단위',
  `LEARNING_RESULT` text DEFAULT NULL,
  `DEPLOY_STATUS` varchar(2) DEFAULT '0' COMMENT '0 : 등록 완료, 1 : 학습 데이터 생성 오류, 2 : 학습 중, 3 : 학습 완료, 4 : 학습 오류, 5 : 배포 중, 6 : 배포 완료, 7 : 배포 중지, 8 : 배포 실패, 9 : 학습 데이터 생성 중, 10 : 학습 중지',
  `DEPLOY_DT` datetime DEFAULT NULL COMMENT '배포일',
  `ROLLBACK_DT` datetime DEFAULT NULL COMMENT '롤백일',
  `RESULT_CODE` varchar(100) NOT NULL COMMENT '결과 코드',
  `RESULT_MSG` varchar(500) NOT NULL COMMENT '결과 메시지',
  `CREATE_DTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '생성시간',
  `DELETE_YN` varchar(1) DEFAULT 'N' COMMENT 'N: 정상, Y: 삭제된 데이터',
  `FILE_PATH` varchar(100) DEFAULT NULL COMMENT '파일경로',
  `FILE_NAME` varchar(100) DEFAULT NULL COMMENT '파일명',
  `HDQR_BOB_DCD` char(1) DEFAULT NULL COMMENT '1 본부, 2 영업점',
  `START_DT` datetime DEFAULT NULL COMMENT '시작일자',
  `END_DT` datetime DEFAULT NULL COMMENT '종료일자',
  `LEARNING_TYPE` varchar(20) DEFAULT NULL COMMENT '학습타입',
  `BDGT_ITEX_FRCS_CON_PRECISION` varchar(10) DEFAULT NULL COMMENT '예산비목정보내용 (비목코드)의 정밀도',
  `BDGT_ITEX_FRCS_CON_RECALL` varchar(10) DEFAULT NULL COMMENT '예산비목정보내용 (비목코드)의 재현율',
  `BDGT_ITEX_FRCS_CON_F1_SCORE` varchar(10) DEFAULT NULL COMMENT '예산비목정보내용 (비목코드)의 F1점수(평가지표)',
  `BDGT_BSNS_FRCS_CON_PRECISION` varchar(10) DEFAULT NULL COMMENT '예산사업ID내용 (부점코드-사업-세부사업)의 정밀도',
  `BDGT_BSNS_FRCS_CON_RECALL` varchar(10) DEFAULT NULL COMMENT '예산사업ID내용 (부점코드-사업-세부사업)의 재현율',
  `BDGT_BSNS_FRCS_CON_F1_SCORE` varchar(10) DEFAULT NULL COMMENT '예산사업ID내용 (부점코드-사업-세부사업)의 F1점수(평가지표)',
  `BDGT_PRFR_RSN_FRCS_CON_PRECISION` varchar(10) DEFAULT NULL COMMENT '비목집행사유내용 (비목-집행사유코드)의 정밀도',
  `BDGT_PRFR_RSN_FRCS_CON_RECALL` varchar(10) DEFAULT NULL COMMENT '비목집행사유내용 (비목-집행사유코드)의 재현율',
  `BDGT_PRFR_RSN_FRCS_CON_F1_SCORE` varchar(10) DEFAULT NULL COMMENT '비목집행사유내용 (비목-집행사유코드)의 F1점수(평가지표)',
  `BDGT_EXNS_PAMT_MCD_PRECISION` varchar(10) DEFAULT NULL COMMENT '예산경비지급방법코드의 정밀도',
  `BDGT_EXNS_PAMT_MCD_RECALL` varchar(10) DEFAULT NULL COMMENT '예산경비지급방법코드의 재현율',
  `BDGT_EXNS_PAMT_MCD_F1_SCORE` varchar(10) DEFAULT NULL COMMENT '예산경비지급방법코드의 F1점수(평가지표)',
  `ACIM_CON_PRECISION` varchar(10) DEFAULT NULL COMMENT '계좌정보내용의 정밀도',
  `ACIM_CON_RECALL` varchar(10) DEFAULT NULL COMMENT '계좌정보내용의 재현율',
  `ACIM_CON_F1_SCORE` varchar(10) DEFAULT NULL COMMENT '계좌정보내용의 F1점수(평가지표)',
  `DEPLOY_STOP_DT` datetime DEFAULT NULL COMMENT '배포 중지일',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=212 DEFAULT CHARSET=utf8mb4 COMMENT='모델관리 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LEARNING_MODEL`
--

LOCK TABLES `LEARNING_MODEL` WRITE;
/*!40000 ALTER TABLE `LEARNING_MODEL` DISABLE KEYS */;
/*!40000 ALTER TABLE `LEARNING_MODEL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LEARNING_MODEL_INPUT`
--

DROP TABLE IF EXISTS `LEARNING_MODEL_INPUT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LEARNING_MODEL_INPUT` (
  `COL_NAME` varchar(100) DEFAULT NULL COMMENT '컬럼명',
  `MODEL_ID` int(11) DEFAULT NULL COMMENT '모델ID',
  `INOUT_GBN` varchar(20) DEFAULT NULL COMMENT '인/아웃 구분',
  `SNO` int(11) DEFAULT NULL COMMENT '순번',
  KEY `LEARNING_MODEL_INPUT_LEARNING_MODEL_FK` (`MODEL_ID`),
  CONSTRAINT `LEARNING_MODEL_INPUT_LEARNING_MODEL_FK` FOREIGN KEY (`MODEL_ID`) REFERENCES `LEARNING_MODEL` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='모델 피처 관리 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LEARNING_MODEL_INPUT`
--

LOCK TABLES `LEARNING_MODEL_INPUT` WRITE;
/*!40000 ALTER TABLE `LEARNING_MODEL_INPUT` DISABLE KEYS */;
/*!40000 ALTER TABLE `LEARNING_MODEL_INPUT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LEARNING_SCHEDULER`
--

DROP TABLE IF EXISTS `LEARNING_SCHEDULER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LEARNING_SCHEDULER` (
  `SCHED_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '학습 스케줄러 ID',
  `SCHED_NM` varchar(500) NOT NULL COMMENT '학습 스케줄러명',
  `HDQR_BOB_DCD` char(4) DEFAULT NULL COMMENT '학습 대상 : 1 본부, 2 영업점',
  `TERM_TY` varchar(20) DEFAULT NULL COMMENT '학습 주기',
  `ST_YMD` date DEFAULT NULL COMMENT '학습 시작 일자',
  `ST_TIME` time DEFAULT NULL COMMENT '학습 시작 시간',
  `USE_AT` char(1) DEFAULT 'N' COMMENT '사용여부',
  `REG_ID` varchar(100) DEFAULT NULL COMMENT '등록자',
  `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록일시',
  `MOD_ID` varchar(100) DEFAULT NULL COMMENT '수정자',
  `MOD_DT` datetime DEFAULT current_timestamp() COMMENT '수정일자',
  `DEL_AT` char(1) DEFAULT 'N' COMMENT '삭제여부',
  `TEMPLATE_ID` int(11) NOT NULL COMMENT '템플릿 ID',
  `DES` varchar(500) DEFAULT NULL COMMENT '배치 설명',
  `EPOCH` varchar(100) DEFAULT NULL COMMENT '학습 데이터 셋을 학습하는 회수',
  `LEARNING_RATE` varchar(100) DEFAULT NULL COMMENT '학습률 , 최적 값을 찾기 위한 기울기의 이동 정도',
  `BATCH_SIZE` varchar(100) DEFAULT NULL COMMENT '학습 데이터셋 중 , 몇 개의 데이터를 묶어서 가중치 값을 갱신할 지 지정하는 단위',
  `SEARCH_MM` varchar(2) DEFAULT NULL COMMENT '조회기간(월)',
  `RUN_CNT` int(4) NOT NULL DEFAULT 0,
  `LATEST_RUN_DT` datetime DEFAULT NULL COMMENT '최근 동작 일시',
  PRIMARY KEY (`SCHED_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb3 COMMENT='학습 스케줄러 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LEARNING_SCHEDULER`
--

LOCK TABLES `LEARNING_SCHEDULER` WRITE;
/*!40000 ALTER TABLE `LEARNING_SCHEDULER` DISABLE KEYS */;
/*!40000 ALTER TABLE `LEARNING_SCHEDULER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TEMPLATE`
--

DROP TABLE IF EXISTS `TEMPLATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TEMPLATE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TEMPLATE_NAME` varchar(500) DEFAULT NULL COMMENT '템플릿명',
  `SELECT_CON` longtext DEFAULT NULL COMMENT '선택조건',
  `HDQR_BOB_DCD` char(1) DEFAULT NULL COMMENT '1 본부, 2 영업점',
  `REG_ID` varchar(100) DEFAULT NULL COMMENT '등록자',
  `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록일자',
  `MOD_ID` varchar(100) DEFAULT NULL COMMENT '수정자',
  `MOD_DT` datetime DEFAULT NULL COMMENT '수정일자',
  `DEL_YN` char(1) DEFAULT NULL COMMENT '삭제여부',
  `LEARNING_TYPE` varchar(20) DEFAULT NULL COMMENT '학습타입',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8mb3 COMMENT='템플릿관리 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TEMPLATE`
--

LOCK TABLES `TEMPLATE` WRITE;
/*!40000 ALTER TABLE `TEMPLATE` DISABLE KEYS */;
/*!40000 ALTER TABLE `TEMPLATE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TEMPLATE_INPUT`
--

DROP TABLE IF EXISTS `TEMPLATE_INPUT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TEMPLATE_INPUT` (
  `COL_NAME` varchar(100) DEFAULT NULL COMMENT '컬럼명',
  `TEMPLATE_ID` int(11) DEFAULT NULL COMMENT '템플릿ID',
  `INOUT_GBN` varchar(20) DEFAULT NULL COMMENT '인/아웃 구분',
  `SNO` int(11) DEFAULT NULL COMMENT '순번',
  KEY `LEARNING_MODEL_INPUT_LEARNING_MODEL_FK` (`TEMPLATE_ID`) USING BTREE,
  CONSTRAINT `TEMPLATE_INPUT_TEMPLATE_FK` FOREIGN KEY (`TEMPLATE_ID`) REFERENCES `TEMPLATE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='템플릿 피처 관리 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TEMPLATE_INPUT`
--

LOCK TABLES `TEMPLATE_INPUT` WRITE;
/*!40000 ALTER TABLE `TEMPLATE_INPUT` DISABLE KEYS */;
/*!40000 ALTER TABLE `TEMPLATE_INPUT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_USAGE_STAT`
--

DROP TABLE IF EXISTS `USER_USAGE_STAT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_USAGE_STAT` (
  `STAT_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '통계 ID',
  `TYPE` varchar(20) NOT NULL COMMENT '데이터 유형 (전체, 카드, 세금계산서)',
  `RSRE_YMD` varchar(8) NOT NULL COMMENT '결과등년월일(YYYYMMDD)',
  `RSRE_YM` varchar(6) DEFAULT NULL COMMENT '결과등록년월(YYYYMM)',
  `RSRE_Y` varchar(4) DEFAULT NULL COMMENT '결과등록년(YYYY)',
  `HDQR_BOB_DCD` char(1) DEFAULT NULL COMMENT '본부/영업점 구분 코드 (1 본부, 2 영업점)',
  `TOTAL` int(11) NOT NULL COMMENT 'INPUT 데이터 수',
  `HIT_CNT1` int(11) NOT NULL COMMENT '예산비목정보_적중수',
  `HIT_CNT2` int(11) NOT NULL COMMENT '예산사업ID_적중수',
  `HIT_CNT3` int(11) NOT NULL COMMENT '비목집행사유_적중수',
  `HIT_CNT4` int(11) NOT NULL COMMENT '예산비목정보_예산사업ID_적중수',
  `ARIN_USE_CNT` int(11) NOT NULL COMMENT 'AI 사용 데이터 수',
  `TOTAL_OUTPUT_CNT` int(11) NOT NULL COMMENT '배치 추론을 통해 생성된 추론 데이터 수',
  `FRRG_TS` datetime NOT NULL DEFAULT current_timestamp() COMMENT '최초등록일시',
  `LSMD_TS` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '최초변경일시',
  PRIMARY KEY (`STAT_ID`),
  UNIQUE KEY `uq_type_rsre_ymd_hdqr_bob_dcd` (`TYPE`,`RSRE_YMD`,`HDQR_BOB_DCD`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb3 COMMENT='사용자 활용 현황 통계 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_USAGE_STAT`
--

LOCK TABLES `USER_USAGE_STAT` WRITE;
/*!40000 ALTER TABLE `USER_USAGE_STAT` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_USAGE_STAT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'temp_maummcc'
--
/*!50003 DROP FUNCTION IF EXISTS `fnGetDeptFullPath` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `fnGetDeptFullPath`(deptId VARCHAR(100)) RETURNS varchar(1000) CHARSET utf8mb4
    READS SQL DATA
    DETERMINISTIC
BEGIN

   DECLARE v_full_dept_path VARCHAR(1000);

   IF deptId IS NULL THEN
      RETURN NULL;
   END IF;

   WITH RECURSIVE DEPT_TREE AS (
      SELECT
           A.DEPT_NAME AS DEPT_PATH
           , A.DEPT_ID
      FROM HC_DEPT AS A
      WHERE (PAR_ID IS NULL OR PAR_ID = '')
      UNION ALL
      SELECT
           CASE C.DEPT_PATH
              WHEN 'system' THEN B.DEPT_NAME
              ELSE CONCAT(C.DEPT_PATH,' > ', B.DEPT_NAME)
           END AS DEPT_PATH
           , B.DEPT_ID
      FROM HC_DEPT AS B
      INNER JOIN DEPT_TREE AS C ON B.PAR_ID = C.DEPT_ID
   )
   SELECT mt.DEPT_PATH
       INTO v_full_dept_path
   FROM DEPT_TREE mt
   WHERE mt.DEPT_ID = deptId
   LIMIT 1;

   IF v_full_dept_path IS NULL THEN
      RETURN 'nullval';
   END IF;

   RETURN v_full_dept_path;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `fnGetDeptTree` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `fnGetDeptTree`() RETURNS varchar(50) CHARSET utf8mb3
    READS SQL DATA
BEGIN

   DECLARE v_par_id varchar(50);
      DECLARE v_rno int;
    DECLARE v_tmp_rno int;
    DECLARE v_org_par_id varchar(50);
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET @dept_id = NULL;

    SET v_par_id = @dept_id;
    SET v_rno = 0;
    SET v_org_par_id = '';
    SET v_tmp_rno = 0;

    IF @dept_id IS NULL THEN
      RETURN NULL;
   END IF;

    LOOP

   SELECT
       min(dept.rno)
       , dept.DEPT_ID
       INTO v_tmp_rno, @dept_id
   FROM (
      SELECT
         @ROWNUM:=@ROWNUM+1 AS rno
         , hm.DEPT_ID
         , hm.PAR_ID
      FROM (
         SELECT
            DEPT_ID
            , PAR_ID
         FROM HC_DEPT JOIN ( SELECT @ROWNUM:=0) VARS
         WHERE 1=1
         AND par_id = v_par_id
         ORDER BY dept_depth ASC, dept_order ASC, dept_name ASC
      ) hm
      WHERE 1=1
   ) dept
   WHERE 1=1
   AND dept.rno > v_rno;

    IF @dept_id IS NOT NULL THEN
       SET @level = @level + 1;
    RETURN @dept_id;
    END IF;

    SET @level := @level - 1;

   SELECT
      par_id
      ,dept_id
    INTO v_org_par_id, v_par_id
   FROM HC_DEPT
   WHERE DEPT_ID = v_par_id;

   SELECT
       min(dept.rno)
       , dept.par_id
    INTO v_rno, v_par_id
   FROM (
      SELECT
         @ROWNUM:=@ROWNUM+1 AS rno
         , hm.DEPT_ID
         , hm.DEPT_ORDER
         , hm.par_id
      FROM (
         SELECT
            DEPT_ID
            ,DEPT_ORDER
            , par_id
         FROM HC_DEPT JOIN ( SELECT @ROWNUM:=0) VARS
         WHERE 1=1
         AND par_id = v_org_par_id
         ORDER BY dept_depth ASC, dept_order ASC, dept_name ASC
      ) hm
      WHERE 1=1
   ) dept
   WHERE 1=1
    AND dept.DEPT_ID = v_par_id;
    END LOOP;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `fnGetMenuFullPath` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `fnGetMenuFullPath`(menuId VARCHAR(100)) RETURNS varchar(1000) CHARSET utf8mb4
    READS SQL DATA
    DETERMINISTIC
BEGIN

   DECLARE v_full_menu_path VARCHAR(1000);

    IF menuId IS NULL THEN
      RETURN NULL;
   END IF;

   WITH RECURSIVE MENU_TREE AS (
      SELECT
           A.MENU_NAME AS MENU_PATH
           , A.MENU_ID
      FROM HC_MENU AS A
      where (PAR_ID is NULL OR PAR_ID = 'system') -- 최상의 레벨
      UNION ALL
      SELECT
           CASE C.MENU_PATH
              WHEN 'system' THEN B.MENU_NAME
              ELSE  concat(C.MENU_PATH,' > ', B.MENU_NAME)
              END AS MENU_PATH
            , B.MENU_ID
      FROM HC_MENU AS B
      INNER JOIN MENU_TREE AS C ON B.PAR_ID = C.MENU_ID -- 하위 level : inner join을 통한 재귀쿼리
   )
   SELECT
       mt.MENU_PATH
       INTO v_full_menu_path
   FROM MENU_TREE mt
   WHERE mt.MENU_ID = menuId
   LIMIT 1;

    IF v_full_menu_path IS NULL THEN
      RETURN 'nullval';
   END IF;

   RETURN v_full_menu_path;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `fnGetMenuTree` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `fnGetMenuTree`() RETURNS varchar(50) CHARSET utf8mb3
    READS SQL DATA
BEGIN

   DECLARE v_par_id varchar(50);
      DECLARE v_rno int;
    DECLARE v_tmp_rno int;
    DECLARE v_org_par_id varchar(50);
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET @menu_id = NULL;

    SET v_par_id = @menu_id;
    SET v_rno = 0;
    SET v_org_par_id = '';
    SET v_tmp_rno = 0;

    IF @menu_id IS NULL THEN
      RETURN NULL;
   END IF;

    LOOP

   SELECT
       min(menu.rno)
       , menu.MENU_ID
       INTO v_tmp_rno, @menu_id
   FROM (
      SELECT
         @ROWNUM:=@ROWNUM+1 AS rno
         , hm.MENU_ID
         , hm.PAR_ID
      FROM (
         SELECT
            MENU_ID
            , PAR_ID
         FROM HC_MENU JOIN ( SELECT @ROWNUM:=0) VARS
         WHERE 1=1
         AND par_id = v_par_id
         ORDER BY menu_depth ASC, menu_order ASC, menu_name ASC
      ) hm
      WHERE 1=1
   ) menu
   WHERE 1=1
   AND menu.rno > v_rno;

    IF @menu_id IS NOT NULL THEN
       SET @level = @level + 1;
    RETURN @menu_id;
    END IF;

    SET @level := @level - 1;

   SELECT
      par_id
      ,menu_id
    INTO v_org_par_id, v_par_id
   FROM HC_MENU
   WHERE MENU_ID = v_par_id;

   SELECT
       min(menu.rno)
       , menu.par_id
    INTO v_rno, v_par_id
   FROM (
      SELECT
         @ROWNUM:=@ROWNUM+1 AS rno
         , hm.MENU_ID
         , hm.MENU_ORDER
         , hm.par_id
      FROM (
         SELECT
            MENU_ID
            ,MENU_ORDER
            , par_id
         FROM HC_MENU JOIN ( SELECT @ROWNUM:=0) VARS
         WHERE 1=1
         AND par_id = v_org_par_id
         ORDER BY menu_depth ASC, menu_order ASC, menu_name ASC
      ) hm
      WHERE 1=1
   ) menu
   WHERE 1=1
    AND menu.MENU_ID = v_par_id;
    END LOOP;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-13 15:16:32
