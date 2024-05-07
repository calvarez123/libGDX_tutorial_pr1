package com.cristian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final Drop game;
    Texture backgroundTexture;
    OrthographicCamera camera;

    public MainMenuScreen(final Drop game) {
        this.game = game;

        // Cargamos la textura de fondo
        backgroundTexture = new Texture(Gdx.files.internal("inicio.jpeg")); // Asegúrate de tener esta imagen en los assets

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
        // Dibujar la imagen de fondo ajustada a la pantalla completa
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Posicionamos el texto de manera más centrada
        float xCenter = Gdx.graphics.getWidth() / 2 - 150; // Ajusta según el tamaño de tu fuente
        game.font.draw(game.batch, "Welcome to Drop!!!", xCenter, Gdx.graphics.getHeight() / 2 + 50);
        game.font.draw(game.batch, "Tap anywhere to begin!", xCenter, Gdx.graphics.getHeight() / 2);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        // Actualizar la cámara con las nuevas dimensiones
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
        // No olvides liberar recursos
        backgroundTexture.dispose();
    }
}
