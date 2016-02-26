package noxer.games.ballons;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class OrthographicCameraExample implements ApplicationListener {

    static final int WORLD_WIDTH = 100;
    static final int WORLD_HEIGHT = 100;

    private PerspectiveCamera cam;
    private OrthographicCamera cam2;
    private SpriteBatch batch;

    private Sprite mapSprite, arbol;
    private float rotationSpeed;

    @Override
    public void create() {
        rotationSpeed = 0.5f;

        mapSprite = new Sprite(new Texture(Gdx.files.internal("sc_map.png")));
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        arbol = new Sprite(new Texture(Gdx.files.internal("arbol.png")));
        arbol.setPosition(20, 20);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        cam2 = new OrthographicCamera(30, 30 * (h / w));
        cam = new PerspectiveCamera(30, 30, 30*(h/w));

        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        //cam.rotate(45, 1, 0, 0);
        cam.near = 0.000001f;
        
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 0f, -20f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.rotate(-50, 1, 0, 0);
        cam.update();
        arbol.setSize(10, 20);
       // arbol.
        Texture tx = new Texture(Gdx.files.internal("arbol.png"));
        
        //mapSprite.rotate(degrees);

        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        handleInput();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        
        batch.begin();
        mapSprite.draw(batch);
        batch.end();
        
        //cam.rotate(50, 1, 0, 0);
        batch.begin();
        cam2.update();
        batch.setProjectionMatrix(cam2.combined);
        arbol.draw(batch);
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam2.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam2.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-3, 0, 0);
            cam2.translate(-3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(3, 0, 0);
            cam2.translate(3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.translate(0, -3, 0);
            cam2.translate(0, 0, -3);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.translate(0, 3, 0);
            cam2.translate(0, 0, 3);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cam.rotate(-rotationSpeed, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            cam.rotate(rotationSpeed, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            cam.rotate(rotationSpeed, 1, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            cam.rotate(-rotationSpeed, 1, 0, 0);
        }

        cam2.zoom = MathUtils.clamp(cam2.zoom, 0.1f, 100/cam2.viewportWidth);

        float effectiveViewportWidth = cam2.viewportWidth * cam2.zoom;
        float effectiveViewportHeight = cam2.viewportHeight * cam2.zoom;

        cam2.position.x = MathUtils.clamp(cam2.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
        cam2.position.y = MathUtils.clamp(cam2.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
    }

    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = 30f;
        cam.viewportHeight = 30f * height/width;
        cam2.viewportWidth = 30f;
        cam2.viewportHeight = 30f * height/width;
        cam.update();
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        mapSprite.getTexture().dispose();
        batch.dispose();
    }

    @Override
    public void pause() {
    }

}
