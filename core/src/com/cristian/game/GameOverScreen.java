package com.cristian.game;

import static com.cristian.game.GameScreen.dropsGathered;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {
    Texture backgroundTexture;
    final Drop game;

    OrthographicCamera camera;

    public GameOverScreen(final Drop game) {
        this.game = game;
        backgroundTexture = new Texture(Gdx.files.internal("pedio.jpg"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        game.batch.begin();
        float xCenter = Gdx.graphics.getWidth() / 2 - 50;
        float yStart = Gdx.graphics.getHeight() / 2 + 50;

        // Cambio de color a negro y ajustes de escala
        game.font.setColor(Color.BLACK);

        // Dibujar "GAME OVER"
        game.font.getData().setScale(3f); // Tamaño estándar para GAME OVER
        game.font.draw(game.batch, "GAME OVER", xCenter, yStart + 70); // Más espacio entre frases

        // Dibujar "PUNTUACION: "
        game.font.getData().setScale(7f); // Más grande para destacar la puntuación
        game.font.draw(game.batch, "PUNTUACION: " + dropsGathered, xCenter, yStart);

        // Dibujar "Tap to restart"
        game.font.getData().setScale(3F); // Restaurar el tamaño para el texto final
        game.font.draw(game.batch, "Tap to restart", xCenter, yStart - 70); // Más espacio entre frases

        game.batch.end();

        if (Gdx.input.isTouched()) {
            dropsGathered = 0;
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
    }
}
