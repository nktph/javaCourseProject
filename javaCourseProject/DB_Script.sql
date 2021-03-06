USE [master]
GO
/****** Object:  Database [scholarships]    Script Date: 17.12.2021 1:56:29 ******/
CREATE DATABASE [scholarships]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'scholarships', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.SQLEXPRESS\MSSQL\DATA\scholarships.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'scholarships_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.SQLEXPRESS\MSSQL\DATA\scholarships_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [scholarships] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [scholarships].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [scholarships] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [scholarships] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [scholarships] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [scholarships] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [scholarships] SET ARITHABORT OFF 
GO
ALTER DATABASE [scholarships] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [scholarships] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [scholarships] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [scholarships] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [scholarships] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [scholarships] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [scholarships] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [scholarships] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [scholarships] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [scholarships] SET  DISABLE_BROKER 
GO
ALTER DATABASE [scholarships] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [scholarships] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [scholarships] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [scholarships] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [scholarships] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [scholarships] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [scholarships] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [scholarships] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [scholarships] SET  MULTI_USER 
GO
ALTER DATABASE [scholarships] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [scholarships] SET DB_CHAINING OFF 
GO
ALTER DATABASE [scholarships] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [scholarships] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [scholarships] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [scholarships] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [scholarships] SET QUERY_STORE = OFF
GO
USE [scholarships]
GO
/****** Object:  Table [dbo].[coef]    Script Date: 17.12.2021 1:56:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[coef](
	[coef_id] [int] IDENTITY(1,1) NOT NULL,
	[record_id] [int] NOT NULL,
	[description] [nchar](100) NOT NULL,
	[coef] [numeric](18, 2) NOT NULL,
 CONSTRAINT [PK_coef] PRIMARY KEY CLUSTERED 
(
	[coef_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 1, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[faculty]    Script Date: 17.12.2021 1:56:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[faculty](
	[faculty_id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nchar](30) NOT NULL,
 CONSTRAINT [PK_faculty] PRIMARY KEY CLUSTERED 
(
	[faculty_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[scholarship]    Script Date: 17.12.2021 1:56:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[scholarship](
	[record_id] [int] IDENTITY(1,1) NOT NULL,
	[monthYear] [date] NOT NULL,
	[student_id] [int] NOT NULL,
	[nOfD] [int] NOT NULL,
	[scholSum] [numeric](18, 2) NOT NULL,
 CONSTRAINT [PK_scholarship] PRIMARY KEY CLUSTERED 
(
	[record_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[specialities]    Script Date: 17.12.2021 1:56:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[specialities](
	[speciality_id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nchar](20) NOT NULL,
 CONSTRAINT [PK_specialities] PRIMARY KEY CLUSTERED 
(
	[speciality_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[students]    Script Date: 17.12.2021 1:56:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[students](
	[student_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[name] [nchar](50) NOT NULL,
	[faculty_id] [int] NOT NULL,
	[speciality_id] [int] NOT NULL,
	[avgScore] [numeric](18, 1) NULL,
 CONSTRAINT [PK_students] PRIMARY KEY CLUSTERED 
(
	[student_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[users]    Script Date: 17.12.2021 1:56:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[users](
	[user_id] [int] IDENTITY(1,1) NOT NULL,
	[login] [nchar](20) NOT NULL,
	[password] [nchar](20) NOT NULL,
	[role] [nchar](10) NOT NULL,
	[isBlocked] [bit] NOT NULL,
 CONSTRAINT [PK_users] PRIMARY KEY CLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [IX_users] UNIQUE NONCLUSTERED 
(
	[login] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[coef]  WITH CHECK ADD  CONSTRAINT [FK_coef_scholarship] FOREIGN KEY([record_id])
REFERENCES [dbo].[scholarship] ([record_id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[coef] CHECK CONSTRAINT [FK_coef_scholarship]
GO
ALTER TABLE [dbo].[scholarship]  WITH CHECK ADD  CONSTRAINT [FK_scholarship_student] FOREIGN KEY([student_id])
REFERENCES [dbo].[students] ([student_id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[scholarship] CHECK CONSTRAINT [FK_scholarship_student]
GO
ALTER TABLE [dbo].[students]  WITH CHECK ADD  CONSTRAINT [FK_student_faculty] FOREIGN KEY([faculty_id])
REFERENCES [dbo].[faculty] ([faculty_id])
GO
ALTER TABLE [dbo].[students] CHECK CONSTRAINT [FK_student_faculty]
GO
ALTER TABLE [dbo].[students]  WITH CHECK ADD  CONSTRAINT [FK_student_speciality] FOREIGN KEY([speciality_id])
REFERENCES [dbo].[specialities] ([speciality_id])
GO
ALTER TABLE [dbo].[students] CHECK CONSTRAINT [FK_student_speciality]
GO
ALTER TABLE [dbo].[students]  WITH CHECK ADD  CONSTRAINT [FK_student_user] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[students] CHECK CONSTRAINT [FK_student_user]
GO
USE [master]
GO
ALTER DATABASE [scholarships] SET  READ_WRITE 
GO
