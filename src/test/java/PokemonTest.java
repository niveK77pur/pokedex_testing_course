package test.java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sql2o.*;

import main.java.Move;
import main.java.Pokemon;

@RunWith(Parameterized.class)
public class PokemonTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  // Source for Parameterized tests:
  // https://www.eviltester.com/post/junit/junit-4-parameterized-tests/
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
      List<Object[]> args = new ArrayList<>();
      args.add(new Object[] { new Pokemon("Squirtle", "Water", "Normal", "A cute turtle", 50.0, 12, 16, false) });
      args.add(new Object[] { new Pokemon("Salamander", "Water", "Normal", "A cute turtle", 50.0, 12, 16, false) });
      args.add(new Object[] { new Pokemon("Kevin", "Water", "Normal", "A cute human", 50.0, 12, 16, false) });
      return args;
  }

  private final Pokemon myPokemon;
  public PokemonTest(Pokemon myPokemon) {
    this.myPokemon = myPokemon;
  }

  @Test
  /* Pokomon instance is of class Pokemon (Unit Test)
   * Create an instance of a Pokemon and make sure it is of type Pokemon.
   */
  public void Pokemon_instantiatesCorrectly_true() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    assertEquals(true, myPokemon instanceof Pokemon);
  }

  @Test
  /* Pokemon name is correctly set (Unit Test)
   * Verify if the name given while instantiating is correctly set and
   * extractable from the instance.
   */
  public void getName_pokemonInstantiatesWithName_String() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    assertEquals("Squirtle", myPokemon.getName());
  }

  @Test
  /* Verify that no Pokemons exist in the beginning (Unit Test)
   * Check that the size of the list of all existing Pokemon is 0.
   */
  public void all_emptyAtFirst() {
    assertEquals(Pokemon.all().size(), 0);
  }

  @Test
  /* Equality of instances (Unit Test)
   * Make sure two instance that were instantiated with the same attributes are
   * considered equal.
   */
  public void equals_returnsTrueIfPokemonAreTheSame_true() {
    Pokemon firstPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    Pokemon secondPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    assertTrue(firstPokemon.equals(secondPokemon));
  }

  @Test
  /* Save a Pokemon (Integration Test)
   * Make sure that saving a pokemon correctly adds it to the database by
   * checking if the size of the list of all Pokemon is 1 after saving the
   * first Pokemon.
   */
  public void save_savesPokemonCorrectly_1() {
    Pokemon newPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    newPokemon.save();
    assertEquals(1, Pokemon.all().size());
  }

  @Test
  /* Retrieve Pokemon from database given ID (Integration Test)
   * Make sure that you can extract a saved Pokemon given its ID. Compare the
   * extracted Pokemon with the one that was saved.
   */
  public void find_findsPokemonInDatabase_true() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    Pokemon savedPokemon = Pokemon.find(myPokemon.getId());
    assertTrue(myPokemon.equals(savedPokemon));
  }

  @Test
  /* Make the Pokemon move (Integration Test)
   * Veryify that the first movement added to a Pokemon is equal to the
   * movement that was given by comparing the movement that is stored in the
   * Pokemon and the one that was provided to the Pokemon.
   */
  public void addMove_addMoveToPokemon() {
    Move myMove = new Move("Punch", "Normal", 50.0, 100);
    myMove.save();
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    myPokemon.addMove(myMove);
    Move savedMove = myPokemon.getMoves().get(0);
    assertTrue(myMove.equals(savedMove));
  }

  @Test
  /* Pokemon gets deleted alongside its moves (Integration Test)
   * When saving a Pokemon and a Move to the database, and adding the move to a
   * Pokemon, we want to make sure the corresponding move is deleted from the
   * database when we delete the Pokemon. This is done by checking that the
   * size of the list of all moves and pokemon is zero after deleting the
   * pokemon.
   */
  public void delete_deleteAllPokemonAndMovesAssociations() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    Move myMove = new Move("Bubble", "Water", 50.0, 100);
    myMove.save();
    myPokemon.addMove(myMove);
    myPokemon.delete();
    assertEquals(0, Pokemon.all().size());
    assertEquals(0, myPokemon.getMoves().size());
  }

  @Test
  /* Retrieve Pokemon from database given name (Integration Test)
   * Make sure that you can extract a saved Pokemon given its name. Compare the
   * extracted Pokemon with the one that was saved.
   */
  public void searchByName_findAllPokemonWithSearchInputString_List() {
    Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
    myPokemon.save();
    assertEquals(myPokemon, Pokemon.searchByName("squir").get(0));
  }

  @Test
  /* Attack Pokemon with Move (Integration Test)
   * Create a Pokemon and a Move. Attack the Pokemon 4 times with the move and
   * make sure the HP decreased accordingly.
   */
  public void fighting_damagesDefender() {
    myPokemon.save();
    myPokemon.hp = 500;
    Move myMove = new Move("Bubble", "Water", 50.0, 100);
    myMove.attack(myPokemon);
    System.out.println(myPokemon.hp);
    myMove.attack(myPokemon);
    System.out.println(myPokemon.hp);
    myMove.attack(myPokemon);
    System.out.println(myPokemon.hp);
    myMove.attack(myPokemon);
    assertEquals(400, myPokemon.hp);
  }

}
