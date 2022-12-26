package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

	private UserController userController = new UserController();
	private FilmController filmController = new FilmController();

	@Test
	public void emptyFilmNameTestAndLongDescription () {

		Film incorrectName = Film.builder()
				.name("")
				.description("film")
				.duration(200)
				.releaseDate(LocalDate.now())
				.build();

		assertThrows(ValidationException.class, ()->filmController.createFilm(incorrectName));

		Film incorrectDesc = Film.builder()
				.name("Blade runner")
				.description("In 2019 Los Angeles, former police officer Rick Deckard is detained by Officer Gaff, who likes to"+
						"make origami figures, and is brought to his former supervisor, Bryant." +
						"Deckard, whose job as a blade runner")
				.releaseDate(LocalDate.parse("1982-07-25"))
				.duration(200)
				.build();

		assertThrows(ValidationException.class, ()->filmController.createFilm(incorrectDesc));


	}

	@Test
	public void wrongFilmDateTestAndNegativeDuration () {
		Film badDateFilm = Film.builder()
				.name("Krtek")
				.description("Чешский мультфильм про крота")
				.releaseDate(LocalDate.parse("1111-05-11"))
				.duration(400)
				.build();

		assertThrows(ValidationException.class, ()-> filmController.createFilm(badDateFilm));

		Film negDurFilm = Film.builder()
				.name("There will be blood")
				.description("Drama film written and directed by Paul Thomas Anderson")
				.releaseDate(LocalDate.parse("2007-09-27"))
				.duration(-5)
				.build();

		assertThrows(ValidationException.class, ()->filmController.createFilm(negDurFilm));
	}

	@Test
	public void emptyUserMailAndIncorrectLoginTest (){
		User emptyMail = User.builder()
				.email("")
				.login("Petr")
				.name("Petya")
				.birthday(LocalDate.parse("1996-05-06"))
				.build();

		assertThrows(ValidationException.class, ()-> userController.createUser(emptyMail));

		User incLog = User.builder()
				.email("mail@.com")
				.login("Petr s probelom")
				.name("Petya")
				.birthday(LocalDate.parse("1998-04-15"))
				.build();

		assertThrows(ValidationException.class, ()-> userController.createUser(incLog));


	}

	@Test
	public void incorrectBirthDate() {
		User incBirth = User.builder()
				.email("@mail.com")
				.login("Darya")
				.name("Darya")
				.birthday(LocalDate.parse("2029-10-11"))
				.build();

		assertThrows(ValidationException.class, ()-> userController.createUser(incBirth));
	}



}
