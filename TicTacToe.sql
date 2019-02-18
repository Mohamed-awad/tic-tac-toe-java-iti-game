-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Feb 18, 2019 at 03:13 PM
-- Server version: 10.1.37-MariaDB
-- PHP Version: 7.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `TicTacToe`
--

-- --------------------------------------------------------

--
-- Table structure for table `games`
--

CREATE TABLE `games` (
  `game_id` int(3) NOT NULL,
  `player1` varchar(50) NOT NULL,
  `player2` varchar(50) NOT NULL,
  `cell11` char(1) NOT NULL,
  `cell12` char(1) NOT NULL,
  `cell13` char(1) NOT NULL,
  `cell21` char(1) NOT NULL,
  `cell22` char(1) NOT NULL,
  `cell23` char(1) NOT NULL,
  `cell31` char(1) NOT NULL,
  `cell32` char(1) NOT NULL,
  `cell33` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `games`
--

INSERT INTO `games` (`game_id`, `player1`, `player2`, `cell11`, `cell12`, `cell13`, `cell21`, `cell22`, `cell23`, `cell31`, `cell32`, `cell33`) VALUES
(75, 'motaz', 'moh', '', '', '', '', 'X', '', '', 'O', ''),
(82, 'moh', 'MotazA', '', '', '', '', '', '', '', '', ''),
(85, 'motaz', 'MotazA', 'X', 'O', '', '', 'O', 'X', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `players`
--

CREATE TABLE `players` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `pass` varchar(50) DEFAULT NULL,
  `status` enum('1','0') DEFAULT NULL,
  `score` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `players`
--

INSERT INTO `players` (`id`, `username`, `pass`, `status`, `score`) VALUES
(12, 'abdo', 'abdo', '1', 0),
(2, 'awad', '01111451253', '0', 0),
(8, 'hesham', '123456789', '1', 0),
(13, 'hhhhhhhhhhhhh', 'hhhhhhhhhhh', '1', 0),
(10, 'KK', 'kk', '1', 0),
(9, 'kolaif', 'logy', '1', 0),
(1, 'medo', '01111451253', '1', 50),
(5, 'mmmm', 'mmmm', '1', 0),
(7, 'mmmmkkk', '123456789', '1', 0),
(15, 'moh', 'moh', '1', 80),
(3, 'motaz', 'mmmm', '1', 25),
(16, 'MotazA', '1234', '1', 40),
(17, 'MotazB', '1234', '1', 0),
(18, 'motazmm', '12345', '1', 0),
(14, 'pppppppppp', 'ppppppp', '1', 0),
(11, 'yy', 'yy', '1', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `games`
--
ALTER TABLE `games`
  ADD PRIMARY KEY (`game_id`),
  ADD KEY `p1` (`player1`),
  ADD KEY `p2` (`player2`);

--
-- Indexes for table `players`
--
ALTER TABLE `players`
  ADD PRIMARY KEY (`username`) USING BTREE,
  ADD UNIQUE KEY `id` (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `games`
--
ALTER TABLE `games`
  MODIFY `game_id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=86;

--
-- AUTO_INCREMENT for table `players`
--
ALTER TABLE `players`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
