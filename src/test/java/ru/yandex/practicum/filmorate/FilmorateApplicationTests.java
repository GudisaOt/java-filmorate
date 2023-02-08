package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
	private final UserDao userStorage;
	private final FilmDao filmDao;

	@SneakyThrows
	@Test
	public void testCreateAndFindUserById() {

		User user = User.builder()
				.name("us")
				.email("us@.mail.com")
				.login("uss")
				.birthday(LocalDate.of(2022, 12, 31))
				.build();

		User user1 = userStorage.addUser(user);

		assertThat(user1)
				.isNotNull();

	}

	@SneakyThrows
	@Test
	public void testUpdateUser (){
		User user = User.builder()
				.name("us")
				.email("us@.mail.com")
				.login("uss")
				.birthday(LocalDate.of(2022, 12, 31))
				.build();
		User userToUpdate = User.builder()
				.id(1)
				.name("usNew")
				.email("NewUs@.mail.com")
				.login("NewUss")
				.birthday(LocalDate.of(2022, 12, 31))
				.build();

		userStorage.addUser(user);
		User updated = userStorage.updateUser(userToUpdate);

		assertThat(updated)
				.hasFieldOrPropertyWithValue("name", "usNew")
				.hasFieldOrPropertyWithValue("email", "NewUs@.mail.com")
				.hasFieldOrPropertyWithValue("login", "NewUss");
	}

	@SneakyThrows
	@Test
	public void testGetAllUsers () {

		User user = User.builder()
				.name("us")
				.email("us@.mail.com")
				.login("uss")
				.birthday(LocalDate.of(2022, 12, 31))
				.build();
		User user2= User.builder()
				.name("usNew")
				.email("NewUs@.mail.com")
				.login("NewUss")
				.birthday(LocalDate.of(2022, 12, 31))
				.build();
		User user3= User.builder()
				.name("3usNew")
				.email("3NewUs@.mail.com")
				.login("3NewUss")
				.birthday(LocalDate.of(2022, 12, 31))
				.build();

		userStorage.addUser(user);
		userStorage.addUser(user2);
		userStorage.addUser(user3);
		Collection<User> list = userStorage.getAll();

		assertThat(list)
				.isNotEmpty()
				.contains(user2);
	}

	@SneakyThrows
	@Test
	public void testCreateFilmAndFindByIdAndUpdate (){
		Film film = Film.builder()
				.name("film1")
				.description("about")
				.releaseDate(LocalDate.of(2015,11,24))
				.duration(2)
				.mpa(MPARating.builder()
						.id(1)
						.build())
				.genres(List.of(Genre.builder().id(3).build()))
				.build();

		Optional<Film> createdFilm = filmDao.addFilm(film);

		assertThat(createdFilm)
				.isNotNull();

		Film filmToUpd = Film.builder()
				.id(1)
				.name("upd")
				.description("about")
				.releaseDate(LocalDate.of(2015,11,24))
				.mpa(MPARating.builder()
						.id(1)
						.build())
				.genres(List.of(Genre.builder().id(3).build()))
				.duration(4)
				.build();

		Optional<Film> upd = filmDao.updateFilm(filmToUpd);

		assertThat(upd)
				.isNotNull()
				.isPresent()
				.hasValueSatisfying(film1 -> assertThat(film1)
						.hasFieldOrPropertyWithValue("name", "upd")
						.hasFieldOrPropertyWithValue("duration", 4)
						.hasFieldOrPropertyWithValue("description", "about"));
	}

	@SneakyThrows
	@Test
	public void testGetAllFilmsAndLikeFilmAndGetMostPopular (){
		//ооздаем множество пользователей и фильмов
		for (int i = 1; i <= 11; i++) {
			Film film = Film.builder()
					.name("film"+ i)
					.description("about")
					.releaseDate(LocalDate.of(2015,11,24))
					.duration(2)
					.mpa(MPARating.builder()
							.id(1)
							.build())
					.genres(List.of(Genre.builder().id(3).build()))
					.build();
			filmDao.addFilm(film);

			User user = User.builder()
					.name("us"+i)
					.email("us@.mail.com")
					.login("uss")
					.birthday(LocalDate.of(2022, 12, 31))
					.build();
			userStorage.addUser(user);
		}

		Collection<Film> list = filmDao.getAll();

		assertThat(list)
				.isNotEmpty()
				.hasSize(11);

		//ставим лайки фильмам
		for (int i = 1; i <5 ; i++) {
			filmDao.likeFilm(1, i);
		}
		for (int i = 1; i <2 ; i++) {
			filmDao.likeFilm(4, i);
		}
		for (int i = 1; i <3 ; i++) {
			filmDao.likeFilm(7, i);
		}


		Collection<Film> topLiked = filmDao.mostLikedFilms(3);
		assertThat(topLiked)
				.hasSize(3)
				.contains(filmDao.findFilmById(4).get());

	}
}