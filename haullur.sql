-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 25 Sep 2023 pada 02.01
-- Versi server: 10.4.27-MariaDB
-- Versi PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `haullur`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `almarhums`
--

CREATE TABLE `almarhums` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `id_keluarga` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `almarhums`
--

INSERT INTO `almarhums` (`id`, `nama`, `id_keluarga`) VALUES
(13, 'Yai Loso', 16),
(14, 'Mbok War', 16),
(16, 'Bapak Das', 16),
(17, 'Bapak Abd Rohman', 15),
(18, 'Bapak Kalio', 15),
(19, 'Kang Rokhani', 16),
(20, 'Bapak Pon', 15),
(21, 'Ibu Kumi', 16),
(22, 'Bapak Salimen', 15),
(23, 'Bapak Abd Syukur', 15),
(24, 'Yai Saribun', 16),
(25, 'Bu Supiyah', 15),
(26, 'Bu Satuma', 15),
(27, 'Mbok Saiah', 16),
(28, 'Mbok Poninten', 15),
(29, 'Mbok Gemi', 15),
(30, 'Ani Ningsih', 15),
(31, 'Prayitno', 15),
(32, 'Bapak Asmad', 16),
(33, 'Bu Sarpiyah', 15),
(34, 'Bu Kasmi', 15),
(35, 'Bapak Ibrahim', 15),
(36, 'Bapak Marun', 16),
(37, 'Bapak Marsam', 15),
(38, 'Bapak Samadun', 15),
(39, 'Moch Rochim', 15),
(40, 'Bapak Bari', 16),
(41, 'Mbok Arti', 15),
(42, 'Mbah Saeru', 15),
(43, 'Bapak Sukari', 16),
(44, 'Bu Sumini', 15),
(45, 'Ibu Makromah', 16),
(46, 'Bapak Samin', 15),
(47, 'Mbok Cari', 16),
(48, 'Bu Samina', 15),
(49, 'Bapak Daim', 16),
(50, 'Bapak Khamid', 15),
(51, 'Mak Wi', 16),
(52, 'Mbah Alfiah', 16),
(53, 'Bapak Karsiman', 16),
(54, 'Mbok Tun', 16),
(55, 'Ibu Turipah', 16),
(56, 'Mbok Ginten', 16),
(57, 'Yai Kasih', 15),
(58, 'Mbok Murtasih', 16),
(59, 'Bapak Kasim', 15),
(60, 'Bu Satiah', 15),
(61, 'Ibu Sarni', 16),
(62, 'Bapak Ndani', 16),
(63, 'Mbah Sariati', 15),
(64, 'Mbah Kamari', 15),
(65, 'Bapak Karto', 16),
(66, 'Mbah Kaminten', 15),
(67, 'Mbok Lasmina', 16),
(68, 'Bapak Kasemen', 17),
(69, 'Ibu Mariani', 16),
(70, 'Bapak Mustakim', 17),
(71, 'Bapak Irwan Evendi', 16),
(72, 'Bu Eni Suparti', 15),
(73, 'Ibu Paini', 17),
(74, 'Mbok sarpuah', 18),
(75, 'Mbah Darmini', 17),
(76, 'Sunami', 17),
(77, 'Mbah Ten', 15),
(78, 'Yunis Deriansa', 16),
(79, 'Kayatin', 17),
(80, 'Mak tasripah', 18),
(81, 'M.Suhartono', 17),
(82, 'Ramun', 17),
(83, 'Mbok Aminten', 16),
(84, 'Wagiti', 17),
(85, 'Bapak Tamin', 15),
(86, 'Ibu Siana', 16),
(87, 'Sunariyah', 17),
(88, 'Nur Hayati', 18),
(89, 'Bapak Warno', 15),
(90, 'Mbik Darsih', 17),
(91, 'Mbah Sarimo', 15),
(92, 'Bapak Abdul Syukur', 16),
(93, 'Siti Rohmah', 18),
(94, 'Bu Kamsah', 17),
(95, 'Joko Siswo', 15),
(96, 'Bapak Taman', 17),
(97, 'Ibu Sumia', 16),
(98, 'Bpk Daim', 18),
(99, 'Mbok Darmina', 17),
(100, 'Bapak Simin', 16),
(101, 'Mbok Tarsih', 17),
(102, 'Mbok Kar', 16),
(103, 'Bapak Gimin', 17),
(104, 'Ibu Sukariyah', 17),
(105, 'Ibu Endang', 15),
(106, 'Bapak Mustakim', 17),
(107, 'Mbok Darsih', 17),
(108, 'Yai Karsan', 17),
(109, 'Ibu Termini', 15),
(110, 'Bapak Wisnan', 16),
(111, 'Khamim', 17),
(112, 'Mbah Satemon', 15),
(113, 'Bapak Isman', 16),
(114, 'Bapak Bakri', 16),
(115, 'Bapak Kasbun', 15),
(116, 'Mbah Alima', 16),
(117, 'Anak Bayu', 16),
(118, 'Bapak Jumain', 17),
(119, 'Mbok Watini', 16),
(120, 'Bapak Rasio', 15),
(121, 'Bapak Prayitno', 15),
(122, 'Hariati', 19),
(123, 'Yai Drawi', 18),
(124, 'Bu Supiyah', 15),
(125, 'Yai kasbun', 18),
(126, 'Siti Aminah', 19),
(127, 'Bu Satukah', 15),
(128, 'Sri Bawon', 19),
(129, 'Mbok kamsiyah', 18),
(130, 'Ibu Poniti', 15),
(131, 'Sadi Purnomo', 19),
(132, 'Bpk Madurakim', 18),
(133, 'Ibu Mesti', 15),
(134, 'Bapak Tajam', 19),
(135, 'Bapak Kasiadi', 15),
(136, 'Mbok Kasemi', 18),
(137, 'Ibu Maimunah', 15),
(138, 'Mbok sukaini', 18),
(139, 'Bu Siti', 15),
(140, 'Bapak Samsuri', 15),
(141, 'Yai Saridah', 19),
(142, 'Ibu Halimah', 15),
(143, 'Yai Li', 20),
(144, 'Yai Warimen', 18),
(145, 'Yai Sateb', 20),
(146, 'Mbok Saripah', 19),
(147, 'Ibu Ismah', 15),
(148, 'Yai Ri', 20),
(149, 'Yai Nam', 20),
(150, 'Mbah Kaji Tumini', 19),
(151, 'Mbah Maun', 19),
(152, 'Yai Run', 20),
(153, 'Nyai Li', 20),
(154, 'Mbah Langkir', 19),
(155, 'Nyai Nam', 20),
(156, 'Jumik', 20),
(157, 'Mbah Bargasi', 19),
(158, 'Sayuti', 20),
(159, 'Mbah Sumo', 19),
(160, 'Agus Salim', 18),
(161, 'Mbah Yam', 19),
(162, 'Mbh Warten', 18),
(163, 'Mbok Tun', 20),
(164, 'Bapak Rohmat', 19),
(165, 'Ibu Mursiah', 18),
(166, 'Mbok Marni', 20),
(167, 'Suandi', 19),
(168, 'Bapak Suparman', 20),
(169, 'Adek Sunanik', 18),
(170, 'Bapak Taji', 19),
(171, 'Bapak Sairon', 20),
(172, 'Nyai Rafik', 20),
(173, 'Selvi', 20),
(174, 'Mas Rudi Hartono', 19),
(175, 'Moch Sholeh', 18),
(176, 'Selva', 20),
(177, 'Mahfud', 20),
(178, 'Patanah', 20),
(179, 'Darimun', 20),
(180, 'Musripah', 20),
(181, 'Mbak Umi', 20),
(182, 'Panawi', 20),
(183, 'Arjo', 20),
(184, 'Nyai Muarti', 21),
(185, 'Juri', 20),
(186, 'Dolah', 20),
(187, 'Yai Salidin', 21),
(188, 'Ibu Rusmini', 20),
(189, 'Bpk Samiyun', 21),
(190, 'Supri', 21),
(191, 'Ibu Tutut', 20),
(192, 'Yai Warni', 21),
(193, 'Nyai Warni', 21),
(194, 'Mbok Sati`ah', 19),
(195, 'Bapak Alipan', 20),
(196, 'Bpk Temen', 21),
(197, 'Nyai Puk', 21),
(198, 'Yai Arso', 21),
(199, 'Mbak Harlis Saadah', 20),
(200, 'Nyai Karinah', 21),
(201, 'Sudarmono', 21),
(202, 'Bpk Jauri', 21),
(203, 'yai Rofii', 21),
(204, 'Moch Imam Fauzi', 23),
(205, 'Mbok Patemah', 21),
(206, 'Bapak Kasuwan', 22),
(207, 'Moch Abdul Kosim', 23),
(208, 'Mbok Saini', 21),
(209, 'Mbok Marlina', 22),
(210, 'Mbok Marlina', 23),
(211, 'Mbok Tun', 21),
(212, 'Bapak Mesto', 23),
(213, 'Bapak Tamun', 22),
(214, 'Bapak Tarub', 23),
(215, 'Bapak Paimun', 22),
(216, 'Pak Sai', 21),
(217, 'Mbah Pateni', 23),
(218, 'Rameli', 21),
(219, 'Nyari Rengget', 22),
(220, 'Rifatul Ghusnia', 23),
(221, 'Yai Lasiman', 23),
(222, 'Bapak Sholeh', 22),
(223, 'Mas ud', 21),
(224, 'Yai Li', 23),
(225, 'Yai Sateb', 23),
(226, 'Siti Rumakyah', 22),
(227, 'Nyai Li', 23),
(228, 'Ibu Riani', 21),
(229, 'Holili', 21),
(230, 'Bapak Ponirun', 22),
(231, 'Nuril Abidah', 23),
(232, 'Mbok saini', 21),
(233, 'Bpk akub', 21),
(234, 'Mbok Juwariyah', 21),
(235, 'Buyut Warijah', 23),
(236, 'Mbak Lasemi', 23),
(237, 'Ibu Rusmini', 23),
(238, 'Yai Kasturi', 23),
(239, 'Yai Nuriyah', 23),
(240, 'Mbok Waginten', 23),
(241, 'Mbok Lasimah', 23),
(242, 'Mbok Rusmini', 23),
(243, 'Mbok Sanatun', 23),
(244, 'Bapak Drais', 25),
(245, 'Sumiati', 22),
(246, 'Bpk Ari', 24),
(247, 'Riami', 22),
(248, 'Yu Kestin', 25),
(249, 'Ibu Arsia', 24),
(250, 'Bapak Sukari', 26),
(251, 'Mbh Sahri', 24),
(252, 'Bu Makromah', 26),
(253, 'Mbh Maridin', 24),
(254, 'Wagiman', 26),
(255, 'Mbok Ngatri', 22),
(256, 'Yu Patimah', 25),
(257, 'Bapak Mui', 27),
(258, 'Bapak Semu', 22),
(259, 'Mbh Arsudin', 24),
(260, 'Bapak Kemali', 27),
(261, 'Mbh Ismail', 24),
(262, 'Bapak Wagirin', 27),
(263, 'Supardi ', 26),
(264, 'Ibu Rodikyah', 27),
(265, 'Mbh Busidin', 24),
(266, 'Mbok Ngatminah', 22),
(267, 'Nur Hayati ', 26),
(268, 'Moh Imam Fauzi', 28),
(269, 'Mbh Pakah', 24),
(270, 'Maryam', 26),
(271, 'Mbh Sani', 24),
(272, 'Ibu Siatun', 27),
(273, 'Moh Abdul Qosim', 28),
(274, 'Mbok Sapunah', 26),
(275, 'Bpk Rukyan', 24),
(276, 'Ibu Suniah', 27),
(277, 'Moh Zainuri', 28),
(278, 'Bapak Ramelan', 22),
(279, 'Bpk nono', 24),
(280, 'Mbok Rapikyah', 25),
(281, 'Mbok Darinah', 26),
(282, 'Bpk Madi', 24),
(283, 'Bu Sianah', 26),
(284, 'Mbh Slamin', 24),
(285, 'Yai Saman', 25),
(286, 'Mbok Temi', 28),
(287, 'Ibu Jasemi', 22),
(288, 'Bapak Warten', 25),
(289, 'Mbok Sari', 28),
(290, 'Mbh Tilem', 24),
(291, 'Bapak Marda.i', 26),
(292, 'Bapak Rai', 25),
(293, 'Mbok Latifah', 28),
(294, 'Mbh Jumain', 24),
(295, 'Setia Budi', 26),
(296, 'Bapak Sahar', 25),
(297, 'Mbok Marlinah', 28),
(298, 'Mbh Slaman', 24),
(299, 'Mbah Jupri', 22),
(300, 'Bpk Artiman', 26),
(301, 'Mbh Atna', 24),
(302, 'Bapak Prayit', 25),
(303, 'Mbok Nursimah', 26),
(304, 'Mbok Lamijah', 27),
(305, 'Ahmad Sari', 24),
(306, 'Ibu Musofah', 22),
(307, 'Siti Semuna', 24),
(308, 'Ibu Atria', 22),
(309, 'Bapak Senedin', 26),
(310, 'Bapak Ramendun', 25),
(311, 'Bapak Kamari', 22),
(312, 'Mbah Sengari', 25),
(313, 'Mat Hasan', 27),
(314, 'Mbah Yaman', 27),
(315, 'Bapak Slamet', 22),
(316, 'Mbah Tumini', 25),
(317, 'Mbah Rusmini', 27),
(318, 'Bpk Daem', 26),
(319, 'Ibu Rajiyem', 22),
(320, 'Mbok Patemi', 28),
(321, 'Mbok Rapik', 27),
(322, 'Mbh Jeber', 24),
(323, 'Pak Timbang', 27),
(324, 'Mbok Rafik', 28),
(325, 'Bu Alfiah', 26),
(326, 'Mbah Klepat', 22),
(327, 'Mbok Ngatiyah', 27),
(328, 'Mbh Gidin', 24),
(329, 'Yai Sateb', 28),
(330, 'Erina Agustiyah', 25),
(331, 'Bpk Saki', 26),
(332, 'Yai Semat', 28),
(333, 'Bapak Khalil', 22),
(334, 'Mbh Nurjati', 24),
(335, 'Bpk Sarimin', 26),
(336, 'Mbh Naisa', 24),
(337, 'Bapak Daun', 25),
(338, 'Yai Rameli', 28),
(339, 'Mbh Nirman', 24),
(340, 'Mukhalimah', 25),
(341, 'Yai Radimen', 28),
(342, 'Bpk Rosadi', 24),
(343, 'Ibu Sulaikha', 24),
(344, 'Yai Lasiman', 28),
(345, 'Bapak Sariban', 22),
(346, 'Bpk Ngatemin', 26),
(347, 'Nyai Rameli', 28),
(348, 'Suroso', 26),
(349, 'Bapak Basir', 22),
(350, 'Nyai Sarnam', 28),
(351, 'Bapak Mesto', 28),
(352, 'Satulin', 22),
(353, 'Muliatno', 22),
(354, 'Bapak Sopi.i', 28),
(355, 'Bpk Sukari', 29),
(356, 'Bapak Kasim', 28),
(357, 'Mbh Sapunah', 29),
(358, 'Mulyono', 22),
(359, 'Nuri Abidah', 28),
(360, 'Mbok Suratin', 26),
(361, 'Bu Marwi', 29),
(362, 'Bu Riamah', 26),
(363, 'Riati', 28),
(364, 'Ibu Utami', 30),
(365, 'Mbah Darni', 22),
(366, 'Bpk Alpiyah', 29),
(367, 'Darmo', 28),
(368, 'Bpk Rohmad', 26),
(369, 'Bapak Nasikat', 22),
(370, 'Mbak Liati', 30),
(371, 'Ruba.i', 28),
(372, 'Marjoko', 29),
(373, 'Bu Carmirah', 26),
(374, 'Mbh Landep', 29),
(375, 'Ibu Darminah', 22),
(376, 'Deni', 29),
(377, 'Yai Arsimun', 30),
(378, 'Yumik', 28),
(379, 'Muhammad Nur', 29),
(380, 'Bu Jairah', 26),
(381, 'Bapak Nur Hadi', 22),
(382, 'Mbok Siti', 30),
(383, 'Rifatul Ghusniah', 28),
(384, 'Bpk Ngadiono', 29),
(385, 'Yai Laseno', 30),
(386, 'Yai Dollah', 26),
(387, 'Ibu Nasrik', 22),
(388, 'Buyut Warijah', 28),
(389, 'Mbok Tiara', 30),
(390, 'Mbh Khamid', 29),
(391, 'Mbak Mardiyah', 26),
(392, 'Ibu Marsuci', 28),
(393, 'Mbah Miseruri', 22),
(394, 'Bapak Tarub', 28),
(395, 'Moh Slamet', 28),
(396, 'Dzakir Almer Daifullah', 29),
(397, 'Mak John', 26),
(398, 'Bu Niayah', 25),
(399, 'Mak Sumiatin', 26),
(400, 'Bapak Suwandi', 25),
(401, 'Mbah Muliati', 22),
(402, 'Putu Sundari ', 26),
(403, 'Bapak Rahmad Hasim', 25),
(404, 'Ibu Istiani', 22),
(405, 'Ibu Sundari', 29),
(406, 'Bapak Yusuf', 25),
(407, 'Mak Ginten', 29),
(408, 'Mbah Lasina', 25),
(409, 'Ibu Wuwul Kurniasari', 22),
(410, 'Mbah Parto Darmin', 29),
(411, 'Bapak Khamid', 26),
(412, 'Bapak Ngadiono', 22),
(413, 'Mbah Tayib', 25),
(414, 'Bu Riyamah', 29),
(415, 'Pak Wajib', 26),
(416, 'Ibu Buasri', 25),
(417, 'Bapak Daim', 29),
(418, 'Ibu Darmani', 22),
(419, 'Wak Riamah ', 26),
(420, 'Bapak Johan', 29),
(421, 'Mbok Minten', 29),
(422, 'Bapak Nodo', 25),
(423, 'Muhammad Nur', 29),
(424, 'Mbah Kiswanto', 22),
(425, 'Dzaka Daifullah', 29),
(426, 'Mbah Sima', 22),
(427, 'Ibu Sitiana', 28),
(428, 'Ginten ', 26),
(429, 'Mbok Ratun', 29),
(430, 'Ibu Kasiati', 22),
(431, 'Mbok Sukati', 26),
(432, 'Mbok Satemi', 28),
(433, 'Pak Munajib', 29),
(434, 'Mbok Artima ', 26),
(435, 'Pak Men', 29),
(436, 'Bapak Mudawam', 28),
(437, 'Bapak Asim', 28),
(438, 'Mbok Yah', 29),
(439, 'Cak Taseri', 26),
(440, 'Mbok Satik', 29),
(441, 'Mat Sapi.i', 28),
(442, 'Mbah Naim', 29),
(443, 'Cak Johan', 26),
(444, 'Ibu Lasmi', 28),
(445, 'Ibu Matromah', 29),
(446, 'Bapak Haji Sulaiman', 28),
(447, 'Mbah H. Muin', 30),
(448, 'Mbok Natun', 28),
(449, 'Mbok Ratun', 26),
(450, 'Ibi Saliamah Binti Said', 29),
(451, 'Bapak Karsan', 31),
(452, 'Mbah Murtosiah', 30),
(453, 'Mohammad Nur', 29),
(454, 'BPK Marsidin', 26),
(455, 'Mbah Nur Ali', 30),
(456, 'Mbok Jainten', 31),
(457, 'Mbah Suwarsih', 30),
(458, 'Ahmad Azzam Nur Wahid', 29),
(459, 'Mbok Poninten', 30),
(460, 'Bapak Sarmo', 31),
(461, 'Ibu Nadirotul Hasanah', 26),
(462, 'Mbok Matar', 30),
(463, 'Ibu Alima', 26),
(464, 'Rokhana', 31),
(465, 'Lilis Suryati', 29),
(466, 'Bapak M. Mansur', 30),
(467, 'Ibu Watina', 26),
(468, 'Minten', 29),
(469, 'Bapak Isqak', 30),
(470, 'Mbok Alimah', 29),
(471, 'Ibu Piani', 31),
(472, 'Ibu Tiana', 25),
(473, 'Bapak Moestofa', 32),
(474, 'Ibu Watini', 29),
(475, 'Bapak Syakur', 32),
(476, 'Aulia Rahmadani', 25),
(477, 'Bapak Mustakim', 31),
(478, 'Bapak Alipi', 32),
(479, 'Bapak Saruwi', 32),
(480, 'Pitono', 31),
(481, 'Bapak Sukari', 33),
(482, 'Bapak Sudarto', 32),
(483, 'Bu Makromah', 33),
(484, 'Bapak Haji Mulyono', 32),
(485, 'Wagiman', 33),
(486, 'Mbah Kastini', 32),
(487, 'Supardi', 33),
(488, 'Adik Sukirno', 32),
(489, 'Nur Hayati', 33),
(490, 'Pak Lek Jaelani', 32),
(491, 'Aluwiono', 31),
(492, 'Maryam', 33),
(493, 'Mbok Sapunah', 33),
(494, 'Pak Abdul Aziz', 32),
(495, 'Mbah Tasmi', 34),
(496, 'Mbok Darinah', 33),
(497, 'Mbok Kinaya', 31),
(498, 'Pak Ahmad', 32),
(499, 'Yai Satib', 32),
(500, 'Mbok Sianah', 33),
(501, 'Bapak Tajam', 35),
(502, 'Anak Nurul Mashudi', 32),
(503, 'Bapak Mardai', 33),
(504, 'Samun', 32),
(505, 'Bapak Kamsun', 31),
(506, 'Bu Liana', 32),
(507, 'Misdi', 31),
(508, 'Mbah Rumijah', 34),
(509, 'Baita', 32),
(510, 'Mbah Mariam', 35),
(511, 'Kasan', 31),
(512, 'Mbah Carikmin', 34),
(513, 'Mbok Rafik', 32),
(514, 'Bu Mos', 32),
(515, 'Bapak Rochmad', 36),
(516, 'Mbah Mat Sari', 34),
(517, 'Bu Riami', 32),
(518, 'Bapak M. Ashari', 36),
(519, 'Said', 31),
(520, 'Bapak H Nur Salim', 35),
(521, 'Bapak Sajid', 36),
(522, 'Bu Muntinah', 34),
(523, 'Bapak Haji Moeljono', 32),
(524, 'Karno', 31),
(525, 'Mbok Atem', 36),
(526, 'Bu Sati', 34),
(527, 'Ibu Siti Nuzulia', 35),
(528, 'Bu Umiati', 34),
(529, 'Ibu Haji Siti Eka Ningsih', 32),
(530, 'Ula bin Fulan', 36),
(531, 'Siti Hindun', 35),
(532, 'Bu Wariatun', 34),
(533, 'Bapak Paimun', 31),
(534, 'Mbah Tasmina', 32),
(535, 'Ibu Poninten', 36),
(536, 'Bapak Sukarno', 32),
(537, 'Yai Salim', 35),
(538, 'Mustar', 34),
(539, 'Ibu Jumiati', 32),
(540, 'Bapak Gusiman', 31),
(541, 'Kasiyanto', 34),
(542, 'Samsul Huda', 35),
(543, 'Bapak Joyo Supratman', 32),
(544, 'Hasan Bisri', 35),
(545, 'Bapak Bunawi', 31),
(546, 'Mbah Sadinah', 32),
(547, 'Mbah Samirah', 32),
(548, 'Tari', 34),
(549, 'Ibu Juwariyah', 36),
(550, 'Siti Munawarah', 35),
(551, 'Ibu Piani', 36),
(552, 'M. Zazuli', 34),
(554, 'Mbok Muriatun', 35),
(555, 'Bapak Suhadi', 31),
(556, 'Sucipto', 34),
(557, 'Bapak Atim', 36),
(558, 'Bapak Tawi', 34),
(559, 'Bapak Kasiyan', 36),
(560, 'Mbok Supini', 31),
(561, 'Bapak Sarip', 34),
(562, 'Bapak Ilyas', 34),
(563, 'Yai Tasrib', 35),
(564, 'Bapak Salam', 31),
(565, 'Mbok Tarmi', 35),
(566, 'Mbok Darsih', 31),
(567, 'Mbah Asmawi', 34),
(568, 'Mbah Sumini', 34),
(569, 'Mbok Sulama', 31),
(570, 'Mbok Jainah', 34),
(571, 'Mbah Sari.ah', 37),
(572, 'Yai Salam', 37),
(573, 'Bapak Dait', 34),
(574, 'Yai Rameli', 38),
(575, 'Mbok Kariyem', 34),
(576, 'Yai Girin', 38),
(577, 'Yai Kader', 37),
(578, 'Mbah Rubinah', 34),
(579, 'Bapak Kasseri', 38),
(580, 'Mbok Kamsina', 31),
(581, 'Siti Romlah', 34),
(582, 'Mak Sariati', 38),
(583, 'Cak Satuman', 38),
(584, 'Ibu Iswari', 34),
(585, 'Mbok Kominten', 38),
(586, 'Bapak Marja.i', 31),
(587, 'Setia Budi', 33),
(588, 'Yai Marsinah ', 38),
(589, 'Bapak Artiman', 33),
(590, 'Mbok Kamini', 31),
(591, 'Mbok Nursimah', 33),
(592, 'Bapak Senedin', 33),
(593, 'Bapak Sunarko', 34),
(594, 'Bapak Aklar', 38),
(595, 'Bapak Daem', 33),
(596, 'Ibu Taminah', 31),
(597, 'Bu Alfiah', 33),
(598, 'Bapak Sukur', 37),
(599, 'Mbak Bae.ah', 38),
(600, 'Bapak Saki', 33),
(601, 'Bapak Sariat', 37),
(602, 'Bapak Santo (Lawang)', 31),
(603, 'Bapak Mutasin', 34),
(604, 'Mbok Siah', 35),
(605, 'Bapak Sarimin', 33),
(606, 'Mbah Karsih', 34),
(607, 'Mbok Samiatun', 37),
(608, 'Ibu Arba.ina', 38),
(609, 'Andik Widodo', 35),
(610, 'Bapak Ngatemin', 33),
(611, 'Bu Mukaya', 37),
(612, 'Siyanto', 33),
(613, 'H. Mas.ul Ashad', 31),
(614, 'Bapak Satam', 35),
(615, 'Suroso', 33),
(616, 'Yai Girun', 38),
(617, 'Susriani', 37),
(618, 'Mbok Suratin', 33),
(619, 'Bu Riamah', 33),
(620, 'Haji Ibrahim', 37),
(621, 'Siti Zulaikha ', 38),
(622, 'Mbok Tun', 37),
(623, 'Bapak Rahmad', 33),
(624, 'Yai Lan', 37),
(625, 'Mbok Aminah', 38),
(626, 'Bapak Sahid', 37),
(627, 'Yai Mat Daib', 35),
(628, 'Bu Carmirah', 33),
(629, 'Bapak Darsam ', 38),
(630, 'Bu Jairah', 33),
(631, 'Mbah Supiyah', 35),
(632, 'Yai Dollah', 33),
(633, 'H. Abdul Rokhim', 38),
(634, 'Ibu Wasikah', 37),
(635, 'Ibu Lutfia', 35),
(636, 'Mbok Siamah', 39),
(637, 'Bu Arifah', 38),
(638, 'Mbok Mardiyah', 33),
(639, 'Ibu Hj Komariah', 35),
(640, 'Bapak Soleh', 35),
(641, 'Ibu Mutmainah', 35),
(642, 'Mbah Hj Sateni', 35),
(643, 'Eko Wahyudi', 35),
(644, 'Hj Widarti', 35),
(645, 'Mak John', 33),
(646, 'Mak Sumiatin', 33),
(647, 'Putu Sundari', 33),
(648, 'Ja.far Asidiq', 38),
(649, 'Bapak Khamid', 33),
(650, 'Pak Wajib', 33),
(651, 'Wak Riamah', 33),
(652, 'Ginten', 33),
(653, 'Ibu Arbainah', 38),
(654, 'Mbok Sukati', 33),
(655, 'Ibu Mariyani', 37),
(656, 'Mbok Artimah', 33),
(657, 'Ibu Sana', 39),
(658, 'Bapak Sapawi', 37),
(659, 'Cak Taseri', 33),
(660, 'Mbok Warinah', 39),
(661, 'Cak Johan', 33),
(662, 'Bapak Patimin', 37),
(663, 'Mbok Ratun', 33),
(664, 'Bapak Drai', 40),
(665, 'Haji Siti Fatimah', 37),
(666, 'Bapak Marsidin', 33),
(667, 'Pak Kaliyo', 37),
(668, 'Ibu Riani', 40),
(669, 'Ibu Supiyah', 37),
(670, 'Ibu Nadirotul Hasanah', 33),
(671, 'Mak Ya /Supiyah', 37),
(672, 'Ibu Alimah', 33),
(673, 'Bapak Mukri Iskandar', 40),
(674, 'Ibu Haji Siti Qomariyah', 37),
(675, 'Ibu Watini', 33),
(676, 'Mbah Saikun', 40),
(677, 'Suprapto', 37),
(678, 'Ibu Subandiah', 39),
(679, 'Satuka', 37),
(680, 'Mbah Aminten', 40),
(681, 'Yai Ramelan', 37),
(683, 'Mbah Madrani', 40),
(684, 'Ibu Ngatmiaten', 39),
(685, 'Mbok Kumiatun', 37),
(686, 'Mbah Tasmini', 40),
(687, 'Bapak Akub', 39),
(688, 'Lek Toha', 37),
(689, 'Ibu Futikhat', 38),
(690, 'Mbah Tasmi', 41),
(691, 'Manshuri', 37),
(692, 'Mbah Daseri', 40),
(693, 'Bapak Anhar', 37),
(694, 'Nanik Setiowati', 39),
(695, 'Bapak Samsuri', 37),
(696, 'Mbah Dasirah', 40),
(697, 'Bapak Katrawi ', 38),
(698, 'Bapak Jalal', 37),
(699, 'Mbah Tari', 41),
(700, 'Mbok Painten', 38),
(701, 'Ibu Arpiaten', 39),
(702, 'Mbah Kapi', 40),
(703, 'Mbah Sarmo', 41),
(704, 'Mbok Lasinah', 39),
(705, 'Yai Siamun', 40),
(706, 'Ari Tri Wardana', 38),
(707, 'Bu Muntinah', 41),
(708, 'Bapak Satemen', 42),
(709, 'Bapak Marsam', 39),
(710, 'Bu Saini', 42),
(711, 'Bu Satia', 42),
(712, 'Bu Rumijah', 41),
(713, 'Bapak Sarkawi', 40),
(714, 'Mbok Kaminten', 39),
(715, 'Mbah Syamsuri', 42),
(716, 'Bapak Taib', 39),
(717, 'Bapak Badri', 40),
(718, 'Mbah Maliha', 42),
(719, 'Bu Tarmi', 41),
(720, 'Mbah Karnadi', 42),
(721, 'Ibu Tiama', 40),
(722, 'Adik Fulaina', 42),
(723, 'Bu Mukayah', 41),
(724, 'Ibu Wartin', 40),
(725, 'Bu Sapuah', 41),
(726, 'Adik Slamet', 42),
(727, 'Bapak Legimoh', 39),
(728, 'Bu Waritun', 41),
(729, 'Bapak Sukur', 43),
(730, 'KH Muhammad Ali', 42),
(731, 'Bapak Tawi', 41),
(732, 'Bapak Sukin', 40),
(733, 'Mbah Sriah', 42),
(734, 'Bapak Ra.is', 43),
(735, 'Bapak Mochtar', 41),
(736, 'Ibu Siati', 40),
(737, 'HJ Siti Fatimah', 42),
(738, 'Bapak Rachmad', 41),
(739, 'Bapak Kayat', 43),
(740, 'Bapak Marpuk', 41),
(741, 'Rahmad Dermawan', 42),
(742, 'Bapak Asim', 40),
(743, 'Mbah Sarikun', 42),
(744, 'Bapak Mi.an', 41),
(745, 'Ibu Asmani', 39),
(746, 'Bapak Sariban', 43),
(747, 'Bapak Buang', 41),
(748, 'Bapak Suriyat', 43),
(749, 'Bapak Toha', 40),
(750, 'Bapak Na.im', 39),
(751, 'Bapak Haji Ibrahim', 43),
(752, 'Ibu Dewi', 40),
(753, 'Bapak Sopyan', 41),
(754, 'Bapak Matsari', 41),
(755, 'Ibu Sulikah', 40),
(756, 'Bapak Kuswandi', 41),
(757, 'Bapak Sami.an', 43),
(758, 'Kasiyanto', 41),
(759, 'Mboo Samiatun', 43),
(760, 'Mbok Kiyah', 41),
(761, 'Ibu Wulandari', 40),
(762, 'Mbok Yamuna', 43),
(763, 'Mbok Paimo', 43),
(764, 'Mbok Atim', 41),
(765, 'Ibu Yunul Khalilah', 40),
(766, 'Bu Mukayatin', 43),
(767, 'Bu Watinah', 43),
(768, 'Sayutik', 40),
(769, 'Mbok Murti', 41),
(770, 'Mbah Sholikhatin', 43),
(771, 'Slamet Budiono', 40),
(772, 'M. Zazuli', 41),
(773, 'Mbok Ti', 39),
(774, 'Zulaikhah', 40),
(775, 'Mbah Salam', 43),
(776, 'Much. Budiono', 39),
(777, 'Yai Kadir', 43),
(778, 'Arizena ', 40),
(779, 'Suprapto', 43),
(780, 'Satukah', 43),
(781, 'Bapak Sanali', 39),
(782, 'Susriani', 43),
(783, 'Mar Atus Sholichah', 44),
(784, 'Kasiani', 43),
(785, 'Bapak Santanu', 39),
(786, 'Arik Subrianto', 40),
(787, 'Toha', 43),
(788, 'Mbok Kamini', 44),
(789, 'Mas.ud', 43),
(790, 'Mbah Yah', 39),
(791, 'Bapak Dulamat', 40),
(792, 'Mbah Matrawi', 44),
(793, 'Markhamah', 43),
(794, 'Mbah Saruki', 44),
(795, 'Anjir Feni Susanto', 39),
(796, 'Bapak Karnadi', 44),
(797, 'Nyai Salamah', 43),
(798, 'Mbok Gina', 44),
(799, 'Sariah', 43),
(800, 'Mbah Sidiq', 44),
(801, 'Haji Fatimah', 43),
(802, 'Yai Kanafi', 39),
(803, 'Mbah Natin', 44),
(804, 'Bapak Samsuri', 43),
(805, 'Mbok Sati ah', 44),
(806, 'Mbah Samsuri', 44),
(807, 'Mbok Warsih', 39),
(808, 'Mbah Supiyah', 43),
(809, 'Ibu Sati ah', 44),
(810, 'Bapak Anhar', 43),
(811, 'Bapak Winoto', 39),
(812, 'Ibu Kustiam', 39),
(813, 'Bapak Satemen', 44),
(814, 'Ibu Malikha', 44),
(815, 'Ibu Khulsum', 39),
(816, 'Mak Dola', 44),
(817, 'Puji Astuti', 44),
(818, 'Ibu Masriah', 44),
(819, 'Bapak Karnadi', 44),
(820, 'Mbok Wagina', 44),
(821, 'Bapak Darsam', 45),
(822, 'Mbok Run', 45),
(823, 'Bapak Rakimin', 45),
(824, 'Bapak Rohmad', 46),
(825, 'Anis Sa.adah', 45),
(826, 'Bapak Samadi', 46),
(827, 'Bu Khanifah', 46),
(828, 'Yas Ikhsan', 46),
(829, 'Mar Atus sholicha', 46),
(830, 'Bapak Saruki', 46),
(831, 'Bapak Dollah', 46),
(832, 'Mbok Kamini', 46),
(833, 'Mbah Natin', 46),
(834, 'Yai Matrawi', 46),
(835, ' Mbak Yu Muslichah', 46),
(836, 'Mbah Temin', 46),
(837, 'Mbah Kasiani', 46),
(838, 'Yai Saniman', 46),
(839, 'Mbok Siti Khalimah', 46),
(840, 'Bapak Arief', 45),
(841, 'Farin', 46),
(842, 'Yai Saliman', 46),
(843, 'Mbok Arief', 45),
(844, 'Yai Diro', 46),
(845, 'Mbok Latipah ', 46),
(846, 'Yai Mardani', 45),
(847, 'Ibu Masriah', 46),
(848, 'Ibu Romlah', 46),
(849, 'Sodik', 45),
(850, 'Mbah Amirun', 46),
(851, 'Mbah Mi.un', 45),
(852, 'Bapak Bai', 45),
(853, 'Nyari Ndari', 45),
(854, 'Bapak Saniwan', 45),
(855, 'Bapak Dulapi', 45),
(856, 'Mbok Sumiati', 45),
(857, 'Mbok Natin', 45),
(858, 'Mbah Saruki', 45),
(859, 'Bapak Lokmar / Ba.i', 45),
(860, 'Bapak Su', 45),
(861, 'Ibu Maschanah', 45),
(862, 'Mak Tini', 41),
(863, 'Yai Dasio', 41),
(864, 'Mbok Jainah', 41),
(865, 'Mbah Bini', 41),
(866, 'Mbah Sres', 41),
(867, 'Mbah Sijian', 41),
(868, 'Budhe Misini', 41),
(869, 'Mbok Wasni', 45),
(870, 'Mbok Kasitun', 45),
(871, 'Mbok Temin', 45),
(872, 'Mbok Kasiani', 45),
(873, 'Bapak Taral', 45),
(874, 'Bapak Talib', 45),
(875, 'Ibu Misri', 45),
(876, 'Ibu Natin', 45),
(877, 'Mbok Nihaya', 45),
(878, 'Nihaya', 45),
(879, 'Ibu Satupa', 45),
(880, 'Ibu Ngateni', 45),
(881, 'Bapak Usman', 45),
(882, 'Ibu Sumiati', 47),
(883, 'Bapak Darsam', 47),
(884, 'Bapak Temin', 47),
(885, 'Bapak Rakimin', 47),
(886, 'Mbok Kasiani', 47),
(887, 'Mbok Ruminah', 47),
(888, 'Mbok Aminten', 47),
(889, 'Bapak Samto', 47),
(890, 'Ibu Jamila / Ibu Jumila', 47),
(891, 'Bapak Saruki', 47),
(892, 'Mbok Boynem', 47),
(893, 'Mbah Satupa', 47),
(894, 'Bapak Ba.i', 47),
(895, 'Much. Nursali', 48),
(896, 'Mursyid', 48),
(897, 'H. Fatchullah', 48),
(898, 'Siti Raimah', 48),
(899, 'Mbok Martuni', 48),
(900, 'Moch. Jubli', 48),
(901, 'Rumsiyah', 48),
(902, 'Ibu Martini', 48),
(903, 'Mbok Nah', 48),
(904, 'Ibu Tatik', 48),
(905, 'Ibu Misnah', 48),
(906, 'Bapak Semu', 48),
(907, 'Ibu Warsini', 48),
(908, 'Ibu Masofah', 48),
(909, 'Ibu Sanimah', 48),
(910, 'Mbok Lasmi', 48),
(911, 'Ibu Lasmi', 48),
(912, 'Mbak Tiha', 48),
(913, 'Bapak H. Shomad', 49),
(914, 'Bapak Sudjarum', 49),
(915, 'Bapak Abdul Rifa.i', 49),
(916, 'Ibu Mariah', 49),
(917, 'Ibu Tuminah', 49),
(918, 'Bapak M. Sholeh', 49),
(919, 'Bapak Supeno', 49),
(920, 'Mbah Marto Kemi', 50),
(921, 'Ibu Sodikyah', 50),
(922, 'Mail', 50),
(923, 'M. Zazuli', 50),
(924, 'Bapak Pairin', 50),
(925, 'Ibu Samuah', 50),
(926, 'Ibu Kamsiah', 50),
(927, 'Bapak Minin', 50),
(928, 'Bu Mutmainah', 50),
(929, 'Bu Suwaidah', 50),
(930, 'Bu Tasmina', 50),
(932, 'TEST', 59);

-- --------------------------------------------------------

--
-- Struktur dari tabel `haul`
--

CREATE TABLE `haul` (
  `id` int(11) NOT NULL,
  `tanggal` varchar(55) NOT NULL,
  `deskripsi` text NOT NULL,
  `status` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `haul`
--

INSERT INTO `haul` (`id`, `tanggal`, `deskripsi`, `status`) VALUES
(14, 'Jumat, 24 Februari 2023', 'Haul Jemuah Legi', 0),
(15, 'Jumat, 24 Maret 2023', 'Haul Akbar Masjid Miftahul Falah', 0),
(16, 'Jumat, 15 September 2023', 'Haul Baru', 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `keluarga`
--

CREATE TABLE `keluarga` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `rt` int(11) NOT NULL,
  `telepon` varchar(15) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `keluarga`
--

INSERT INTO `keluarga` (`id`, `nama`, `rt`, `telepon`, `id_user`) VALUES
(15, 'Bapak Sukarso', 2, '', 0),
(16, 'Bapak Mat Jailani', 2, '', 4),
(17, 'Bapak Jumawi', 2, '', 0),
(18, 'Saudara Darmaji', 2, '', 0),
(19, 'Bapak Diran', 2, '', 0),
(20, 'Bapak Takim', 2, '', 0),
(21, 'H. Maun', 2, '', 0),
(22, 'Bapak Purwanto', 2, '', 0),
(23, 'Bapak Rokai', 2, '', 0),
(24, 'Solikin Rusdianto', 2, '', 0),
(25, 'Ibu Rupik', 2, '', 1),
(26, 'Bpk. Agung', 2, '', 0),
(27, 'Bapak Sodiqin', 2, '', 0),
(28, 'Bapak Samin', 2, '', 0),
(29, 'Ibu Sumarni', 2, '', 0),
(30, 'Bapak Kariono', 2, '', 0),
(31, 'Bapak Karim', 2, '', 1),
(32, 'Bapak Andik/Rudi Yulianto', 2, '', 1),
(33, 'Bapak Siyanto', 2, '', 0),
(34, 'Bapak M. Imam Arip', 2, '', 0),
(35, 'Bapak Misbakhul Isya', 2, '', 0),
(36, 'Bapak Abdilah', 2, '', 0),
(37, 'Bapak Samsuri/Ibu Khotimah', 2, '', 0),
(38, 'Bapak Ayub', 2, '', 1),
(39, 'Bapak Apri', 2, '', 0),
(40, 'Bapak Yan Rakasiwi', 2, '', 0),
(41, 'Bapak H. Mudjiono', 2, '', 4),
(42, 'Bapak Suyuti', 2, '', 0),
(43, 'Bapak Haji Ahmad / Cak Nur', 2, '', 0),
(44, 'Bapak Syukur', 2, '', 0),
(45, 'Bapak Akhadun', 2, '', 4),
(46, 'Bapak Syaiful', 2, '', 0),
(47, 'Bapak Sa.i', 2, '', 0),
(48, 'Bapak Sutik', 2, '', 0),
(49, 'Bapak Rofiq', 2, '', 0),
(50, 'Ibu Tutik', 2, '', 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `penarikan`
--

CREATE TABLE `penarikan` (
  `id` int(11) NOT NULL,
  `id_haul` int(11) NOT NULL,
  `id_keluarga` int(11) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `deskripsi` text NOT NULL,
  `id_user` int(11) NOT NULL,
  `dibaca` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `penarikan`
--

INSERT INTO `penarikan` (`id`, `id_haul`, `id_keluarga`, `jumlah`, `deskripsi`, `id_user`, `dibaca`) VALUES
(25, 14, 32, 25000, '', 1, 0),
(29, 16, 25, 50000, '', 1, 1),
(30, 16, 41, 100000, '', 4, 1),
(31, 16, 45, 20000, '', 4, 1),
(32, 16, 22, 50000, '', 0, 0),
(43, 16, 0, 100000, 'Sumbangan 2', 0, 0),
(44, 14, 33, 25000, '', 0, 0),
(45, 14, 0, 100000, 'Sumbangan', 0, 0),
(46, 14, 39, 50000, '', 0, 0),
(47, 16, 38, 20000, '', 1, 0),
(48, 16, 32, 15000, '', 1, 0),
(50, 16, 39, 25000, '', 0, 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengeluaran`
--

CREATE TABLE `pengeluaran` (
  `id` int(11) NOT NULL,
  `id_haul` int(11) NOT NULL,
  `deskripsi` text NOT NULL,
  `jumlah` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pengeluaran`
--

INSERT INTO `pengeluaran` (`id`, `id_haul`, `deskripsi`, `jumlah`) VALUES
(3, 16, 'Gorengan', 20000),
(4, 16, 'Rokok', 50000),
(5, 16, 'Akua', 20000),
(6, 16, 'Jajan', 40000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `telepon` varchar(15) NOT NULL,
  `sandi` varchar(255) NOT NULL,
  `level` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`id`, `nama`, `email`, `telepon`, `sandi`, `level`) VALUES
(1, 'Rosadi', 'imam.rosadi.153@gmail.com', '087750354305', '123', 1),
(4, 'Fauzul', 'fauzula@gmail.cok', '089', '123', 2),
(6, 'Hilmi', '', '0878273827232', '123', 2),
(7, 'Ipul', '', '08980022272', '123', 1),
(8, 'Rohman', '', '089685720288', '123', 1),
(9, 'Rozi', '', '087841532262', '123', 2),
(10, 'Syarif', '', '089694386868', '123', 2),
(11, 'Rizal', '', '082332366298', '123', 2);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `almarhums`
--
ALTER TABLE `almarhums`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `haul`
--
ALTER TABLE `haul`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `keluarga`
--
ALTER TABLE `keluarga`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `penarikan`
--
ALTER TABLE `penarikan`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `pengeluaran`
--
ALTER TABLE `pengeluaran`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `almarhums`
--
ALTER TABLE `almarhums`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=933;

--
-- AUTO_INCREMENT untuk tabel `haul`
--
ALTER TABLE `haul`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT untuk tabel `keluarga`
--
ALTER TABLE `keluarga`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;

--
-- AUTO_INCREMENT untuk tabel `penarikan`
--
ALTER TABLE `penarikan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT untuk tabel `pengeluaran`
--
ALTER TABLE `pengeluaran`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
