package test.java;

import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.sql2o.*;

import jdk.jfr.Description;

import org.junit.*;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;


public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  // @Rule
  // public DatabaseRule database = new DatabaseRule();
  public DatabaseRule database;
  @BeforeEach
  public void beforeEach() {
      this.database = new DatabaseRule();
  }

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  // @ClassRule
  // public static ServerRule server = new ServerRule();
  public static ServerRule server;
  @BeforeAll
  public void beforeAll() {
      AppTest.server = new ServerRule();
  }


  @Test
  @DisplayName("Correct Page Test (Acceptance Test)")
  @Description("This makes sure the correct page (i.e. Pokedex) is displayed by verifying if 'Pokedex' can be found in the HTML source.")
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Pokedex");
  }

  @Test
  @DisplayName("Display 'The Complete Pokedex' (Acceptance Test)")
  @Description("Makes sure the correct page is displayed by probing two items from the Pokedex.")
  public void allPokemonPageIsDisplayed() {
    goTo("http://localhost:4567/");
    click("#viewDex");
    assertThat(pageSource().contains("Ivysaur"));
    assertThat(pageSource().contains("Charizard"));
  }

  @Test
  @DisplayName("Display information about specific Pokemons (Acceptance Test)")
  @Description("Makes sure the correct page is displayed by probing for the given Pokemon's name.")
  public void individualPokemonPageIsDisplayed() {
    goTo("http://localhost:4567/pokepage/6");
    assertThat(pageSource().contains("Charizard"));
  }

  @Test
  @DisplayName("Cycle to next page with arrow (Integration Test)")
  @Description("Make sure the arrorw cycles to the next pokemon by clicking on the arrow and probing for the expected Pokemon's name.")
  public void arrowsCycleThroughPokedexCorrectly() {
    goTo("http://localhost:4567/pokepage/6");
    click(".glyphicon-triangle-right");
    assertThat(pageSource().contains("Squirtle"));
  }

  @Test
  @DisplayName("Searching for Pokemon in the Pokedex (Integration Test)")
  @Description("Make sure you can search for Pokemon by entering a search term and probing for one expected Pokemon's name.")
  public void searchResultsReturnMatches() {
    goTo("http://localhost:4567/pokedex");
    fill("#name").with("char");
    assertThat(pageSource().contains("Charizard"));
  }

  @Test
  @DisplayName("Searching for non exsistant Pokemon (Integration Test)")
  @Description("Make sure no Pokemon are listed when entering a name that doesn't match with any Pokemon by probing for the 'no matched' message.")
  public void searchResultsReturnNoMatches() {
    goTo("http://localhost:4567/pokedex");
    fill("#name").with("x");
    assertThat(pageSource().contains("No matches for your search results"));
  }

}
