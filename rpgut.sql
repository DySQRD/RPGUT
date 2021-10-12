-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : mar. 12 oct. 2021 à 21:08
-- Version du serveur :  8.0.23
-- Version de PHP : 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `rpgut`
--

-- --------------------------------------------------------

--
-- Structure de la table `connexion`
--

CREATE TABLE `connexion` (
  `id` int NOT NULL,
  `compte_id` int NOT NULL,
  `connection_date` datetime NOT NULL,
  `deconnection_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `consommable`
--

CREATE TABLE `consommable` (
  `id` int NOT NULL,
  `nom` varchar(45) NOT NULL,
  `durabilite` smallint DEFAULT NULL COMMENT 'Nombre d''utilisations de base du consommable.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `consommable_effet`
--

CREATE TABLE `consommable_effet` (
  `consommable_id` bigint NOT NULL,
  `effet_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `joueur`
--

CREATE TABLE `joueur` (
  `id` int NOT NULL,
  `nom` tinytext NOT NULL,
  `mdp` varchar(45) NOT NULL,
  `xp` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `joueur_consommable`
--

CREATE TABLE `joueur_consommable` (
  `joueur_id` int NOT NULL,
  `consommable_id` int NOT NULL,
  `durabilite` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `joueur_mob`
--

CREATE TABLE `joueur_mob` (
  `compte_id` int NOT NULL,
  `mob_id` int NOT NULL,
  `vaincus` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `joueur_niveau`
--

CREATE TABLE `joueur_niveau` (
  `compte_id` int NOT NULL,
  `niveau_id` int NOT NULL,
  `completion_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Enregistre chaque complétion d''un niveau par un joueur.';

-- --------------------------------------------------------

--
-- Structure de la table `mob`
--

CREATE TABLE `mob` (
  `id` int NOT NULL,
  `nom` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `niveau`
--

CREATE TABLE `niveau` (
  `id` int NOT NULL,
  `nom` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `connexion`
--
ALTER TABLE `connexion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `connexion_compte_compte_id` (`compte_id`);

--
-- Index pour la table `consommable`
--
ALTER TABLE `consommable`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `consommable_effet`
--
ALTER TABLE `consommable_effet`
  ADD PRIMARY KEY (`consommable_id`,`effet_id`);

--
-- Index pour la table `joueur`
--
ALTER TABLE `joueur`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `joueur_consommable`
--
ALTER TABLE `joueur_consommable`
  ADD PRIMARY KEY (`consommable_id`,`joueur_id`),
  ADD KEY `joueur_consommable_joueur_id` (`joueur_id`);

--
-- Index pour la table `joueur_mob`
--
ALTER TABLE `joueur_mob`
  ADD PRIMARY KEY (`compte_id`,`mob_id`);

--
-- Index pour la table `joueur_niveau`
--
ALTER TABLE `joueur_niveau`
  ADD PRIMARY KEY (`compte_id`,`niveau_id`),
  ADD KEY `compte_niveau_niveau_id` (`niveau_id`);

--
-- Index pour la table `mob`
--
ALTER TABLE `mob`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `niveau`
--
ALTER TABLE `niveau`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `connexion`
--
ALTER TABLE `connexion`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `consommable`
--
ALTER TABLE `consommable`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `joueur`
--
ALTER TABLE `joueur`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `mob`
--
ALTER TABLE `mob`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `connexion`
--
ALTER TABLE `connexion`
  ADD CONSTRAINT `connexion_compte_compte_id` FOREIGN KEY (`compte_id`) REFERENCES `joueur` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Contraintes pour la table `joueur_consommable`
--
ALTER TABLE `joueur_consommable`
  ADD CONSTRAINT `joueur_consommable_consommable_id` FOREIGN KEY (`consommable_id`) REFERENCES `consommable` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `joueur_consommable_joueur_id` FOREIGN KEY (`joueur_id`) REFERENCES `joueur` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Contraintes pour la table `joueur_niveau`
--
ALTER TABLE `joueur_niveau`
  ADD CONSTRAINT `compte_niveau_compte_id` FOREIGN KEY (`compte_id`) REFERENCES `joueur` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `compte_niveau_niveau_id` FOREIGN KEY (`niveau_id`) REFERENCES `niveau` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
