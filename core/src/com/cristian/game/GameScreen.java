package com.cristian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    final Drop game;

    // Añadiendo contadores y variables para controlar FPS
    float fpsTime = 0;
    int fpsCounter = 0;
    int fps = 0;

    // Texturas y sonidos
    Texture backgroundTexture;
    Texture dropImage;
    Texture bucketImage;
    Sound dropSound, Gameover;
    Music rainMusic;

    OrthographicCamera camera;
    Rectangle bucket;
    Array<Rectangle> raindrops;
    long lastDropTime;
    static int dropsGathered = 0;

    public GameScreen(final Drop game) {
        this.game = game;

        // Carga de texturas para el gota, cubo y fondo
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        backgroundTexture = new Texture(Gdx.files.internal("fondo.jpeg")); // Asegúrate de que el archivo exista

        // Carga de efectos de sonido y música de fondo
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        Gameover = Gdx.audio.newSound(Gdx.files.internal("gameover.wav"));
        rainMusic.setLooping(true);

        // Configuración de la cámara
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Configuración inicial del cubo
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2; // Centrado horizontal
        bucket.y = 20;               // Posicionado a 20px del borde inferior
        bucket.width = 64;
        bucket.height = 64;

        // Inicialización de gotas de lluvia
        raindrops = new Array<Rectangle>();
        spawnRaindrop();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        fpsTime += delta;
        fpsCounter++;

        if (fpsTime >= 1.0f) {
            fps = fpsCounter;
            fpsCounter = 0;
            fpsTime = 0;
        }

        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Dibujar fondo
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, 800, 480);
        game.font.draw(game.batch, "FPS: " + fps, 10, 470);
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 460);
        game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        game.batch.end();

        // Manejo de entradas
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

        // Mantener el cubo dentro de los límites de la pantalla
        bucket.x = MathUtils.clamp(bucket.x, 0, 800 - 64);

        // Gestión de gotas de lluvia
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) {
                iter.remove();
                Gameover.play();
                game.setScreen(new GameOverScreen(game));
            }
            if (raindrop.overlaps(bucket)) {
                dropsGathered++;
                dropSound.play();
                iter.remove();
            }
        }
    }

    @Override
    public void resize(int width, int height) {

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

    }

    // Métodos del ciclo de vida de Screen omitidos para brevedad
}


