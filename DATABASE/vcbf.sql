-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 09, 2014 at 06:59 AM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `vcbf`
--

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_admin`
--

CREATE TABLE IF NOT EXISTS `vcbf_admin` (
  `username` varchar(30) NOT NULL,
  `password` varchar(200) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `vcbf_admin`
--

INSERT INTO `vcbf_admin` (`username`, `password`) VALUES
('vcbf', 'vcbf');

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_information_message`
--

CREATE TABLE IF NOT EXISTS `vcbf_information_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(2000) NOT NULL,
  `content` varchar(10000) NOT NULL,
  `title_vn` varchar(2000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `content_vn` varchar(10000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_nav_bcf`
--

CREATE TABLE IF NOT EXISTS `vcbf_nav_bcf` (
  `week` varchar(10) NOT NULL,
  `nav` double(16,2) NOT NULL,
  PRIMARY KEY (`week`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_nav_tbf`
--

CREATE TABLE IF NOT EXISTS `vcbf_nav_tbf` (
  `week` varchar(10) NOT NULL,
  `nav` double(16,2) NOT NULL,
  PRIMARY KEY (`week`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_news`
--

CREATE TABLE IF NOT EXISTS `vcbf_news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `header` varchar(2000) NOT NULL,
  `content` varchar(10000) NOT NULL,
  `header_vn` varchar(2000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `content_vn` varchar(10000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_portfolio_detail_bcf`
--

CREATE TABLE IF NOT EXISTS `vcbf_portfolio_detail_bcf` (
  `date` date NOT NULL,
  `asset` varchar(700) NOT NULL,
  `asset_vn` varchar(700) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `percentage` double NOT NULL,
  PRIMARY KEY (`date`,`asset`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_portfolio_detail_tbf`
--

CREATE TABLE IF NOT EXISTS `vcbf_portfolio_detail_tbf` (
  `date` date NOT NULL,
  `asset` varchar(700) NOT NULL,
  `asset_vn` varchar(700) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `percentage` double NOT NULL,
  PRIMARY KEY (`date`,`asset`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_portfolio_industry_breakdown_bcf`
--

CREATE TABLE IF NOT EXISTS `vcbf_portfolio_industry_breakdown_bcf` (
  `date` date NOT NULL,
  `industry` varchar(700) NOT NULL,
  `industry_vn` varchar(700) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `percentage` double NOT NULL,
  PRIMARY KEY (`date`,`industry`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_portfolio_industry_breakdown_tbf`
--

CREATE TABLE IF NOT EXISTS `vcbf_portfolio_industry_breakdown_tbf` (
  `date` date NOT NULL,
  `industry` varchar(700) NOT NULL,
  `industry_vn` varchar(700) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `percentage` double NOT NULL,
  PRIMARY KEY (`date`,`industry`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_portfolio_statistic_bcf`
--

CREATE TABLE IF NOT EXISTS `vcbf_portfolio_statistic_bcf` (
  `date` date NOT NULL,
  `net_asset_value` double NOT NULL,
  `turnover_rate` double NOT NULL,
  `total_position` double NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_portfolio_statistic_tbf`
--

CREATE TABLE IF NOT EXISTS `vcbf_portfolio_statistic_tbf` (
  `date` date NOT NULL,
  `net_asset_value` double NOT NULL,
  `turnover_rate` double NOT NULL,
  `total_position` double NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_portfolio_top_holding_bcf`
--

CREATE TABLE IF NOT EXISTS `vcbf_portfolio_top_holding_bcf` (
  `date` date NOT NULL,
  `company_name` varchar(700) NOT NULL,
  `company_name_vn` varchar(700) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `percentage` double NOT NULL,
  PRIMARY KEY (`date`,`company_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_portfolio_top_holding_tbf`
--

CREATE TABLE IF NOT EXISTS `vcbf_portfolio_top_holding_tbf` (
  `date` date NOT NULL,
  `company_name` varchar(700) NOT NULL,
  `company_name_vn` varchar(700) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `percentage` double NOT NULL,
  PRIMARY KEY (`date`,`company_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_reminding_message`
--

CREATE TABLE IF NOT EXISTS `vcbf_reminding_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(2000) NOT NULL,
  `content` varchar(10000) NOT NULL,
  `title_vn` varchar(2000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `content_vn` varchar(10000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_user`
--

CREATE TABLE IF NOT EXISTS `vcbf_user` (
  `username` varchar(700) NOT NULL,
  `password` varchar(700) NOT NULL,
  `investor_name` varchar(2000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `investor_folio_number` varchar(2000) NOT NULL,
  `investor_holding` double NOT NULL,
  `investor_nav` double NOT NULL,
  `investor_value` double NOT NULL,
  `investor_type` varchar(100) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_version`
--

CREATE TABLE IF NOT EXISTS `vcbf_version` (
  `version` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `vcbf_version`
--

INSERT INTO `vcbf_version` (`version`) VALUES
(91);

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_weekly_fund_price_bcf`
--

CREATE TABLE IF NOT EXISTS `vcbf_weekly_fund_price_bcf` (
  `date` date NOT NULL,
  `nav_unit` double NOT NULL,
  `nav_change` double NOT NULL,
  `pop` double NOT NULL,
  `highest_nav_unit` double NOT NULL,
  `lowest_nav_unit` double NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vcbf_weekly_fund_price_tbf`
--

CREATE TABLE IF NOT EXISTS `vcbf_weekly_fund_price_tbf` (
  `date` date NOT NULL,
  `nav_unit` double NOT NULL,
  `nav_change` double NOT NULL,
  `pop` double NOT NULL,
  `highest_nav_unit` double NOT NULL,
  `lowest_nav_unit` double NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
