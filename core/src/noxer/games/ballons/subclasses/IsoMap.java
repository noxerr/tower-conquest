package noxer.games.ballons.subclasses;

import static com.badlogic.gdx.graphics.g2d.Batch.*;
import noxer.games.ballons.entities.Ball;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class IsoMap extends BatchTiledMapRenderer {

	private Matrix4 isoTransform;
	private Matrix4 invIsotransform;
	private Vector3 screenPos = new Vector3();

	private Vector2 topRight = new Vector2();
	private Vector2 bottomLeft = new Vector2();
	private Vector2 topLeft = new Vector2();
	private Vector2 bottomRight = new Vector2();

	public IsoMap (TiledMap map) {
		super(map);
		init();
	}

	public IsoMap (TiledMap map, Batch batch) {
		super(map, batch);
		init();
	}

	private void init () {
		// create the isometric transform
		isoTransform = new Matrix4();
		isoTransform.idt();

		// isoTransform.translate(0, 32, 0);
		isoTransform.scale((float)(Math.sqrt(2.0) / 2.0), (float)(Math.sqrt(2.0) / 4.0), 1.0f);
		isoTransform.rotate(0.0f, 0.0f, 1.0f, -45);

		// ... and the inverse matrix
		invIsotransform = new Matrix4(isoTransform);
		invIsotransform.inv();
	}

	private Vector3 translateScreenToIso (Vector2 vec) {
		screenPos.set(vec.x, vec.y, 0);
		screenPos.mul(invIsotransform);

		return screenPos;
	}

	@Override
	public void renderTileLayer (TiledMapTileLayer layer) {
		final Color batchColor = batch.getColor();
		final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());

		float tileWidth = layer.getTileWidth() * unitScale;
		float tileHeight = layer.getTileHeight() * unitScale;
		float halfTileWidth = tileWidth * 0.5f;
		float halfTileHeight = tileHeight * 0.5f;

		// setting up the screen points
		// COL1
		topRight.set(viewBounds.x + viewBounds.width, viewBounds.y);
		// COL2
		bottomLeft.set(viewBounds.x, viewBounds.y + viewBounds.height);
		// ROW1
		topLeft.set(viewBounds.x, viewBounds.y);
		// ROW2
		bottomRight.set(viewBounds.x + viewBounds.width, viewBounds.y + viewBounds.height);

		// transforming screen coordinates to iso coordinates
		int row1 = (int)(translateScreenToIso(topLeft).y / tileWidth) - 2;
		int row2 = (int)(translateScreenToIso(bottomRight).y / tileWidth) + 2;

		int col1 = (int)(translateScreenToIso(bottomLeft).x / tileWidth) - 2;
		int col2 = (int)(translateScreenToIso(topRight).x / tileWidth) + 2;

		for (int row = row2; row >= row1; row--) {
			for (int col = col1; col <= col2; col++) {
				float x = (col * halfTileWidth) + (row * halfTileWidth);
				float y = (row * halfTileHeight) - (col * halfTileHeight);

				final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
				if (cell == null) continue;
				final TiledMapTile tile = cell.getTile();

				if (tile != null) {
					TextureRegion region = tile.getTextureRegion();

					float x1 = x + tile.getOffsetX() * unitScale;
					float y1 = y + tile.getOffsetY() * unitScale;
					float x2 = x1 + region.getRegionWidth() * unitScale;
					float y2 = y1 + region.getRegionHeight() * unitScale;

					float u1 = region.getU();
					float v1 = region.getV2();
					float u2 = region.getU2();
					float v2 = region.getV();

					vertices[X1] = x1;
					vertices[Y1] = y1;
					vertices[C1] = color;
					vertices[U1] = u1;
					vertices[V1] = v1;

					vertices[X2] = x1;
					vertices[Y2] = y2;
					vertices[C2] = color;
					vertices[U2] = u1;
					vertices[V2] = v2;

					vertices[X3] = x2;
					vertices[Y3] = y2;
					vertices[C3] = color;
					vertices[U3] = u2;
					vertices[V3] = v2;

					vertices[X4] = x2;
					vertices[Y4] = y1;
					vertices[C4] = color;
					vertices[U4] = u2;
					vertices[V4] = v1;
					
					batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
				}
			}
		}
	}


	public void customRender (TiledMapTileLayer layer, int[][] coords, Array<Ball> player) {
		final Color batchColor = batch.getColor();
		final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());

		float tileWidth = layer.getTileWidth() * unitScale;
		float tileHeight = layer.getTileHeight() * unitScale;
		float halfTileWidth = tileWidth * 0.5f;
		float halfTileHeight = tileHeight * 0.5f;

		// setting up the screen points
		// COL1
		topRight.set(viewBounds.x + viewBounds.width, viewBounds.y);
		// COL2
		bottomLeft.set(viewBounds.x, viewBounds.y + viewBounds.height);
		// ROW1
		topLeft.set(viewBounds.x, viewBounds.y);
		// ROW2
		bottomRight.set(viewBounds.x + viewBounds.width, viewBounds.y + viewBounds.height);

		// transforming screen coordinates to iso coordinates
		int row1 = (int)(translateScreenToIso(topLeft).y / tileWidth) - 2;
		int row2 = (int)(translateScreenToIso(bottomRight).y / tileWidth) + 2;

		int col1 = (int)(translateScreenToIso(bottomLeft).x / tileWidth) - 2;
		int col2 = (int)(translateScreenToIso(topRight).x / tileWidth) + 2;

		for (int row = row2; row >= row1; row--) {
			for (int col = col1; col <= col2; col++) {
				float x = (col * halfTileWidth) + (row * halfTileWidth);
				float y = (row * halfTileHeight) - (col * halfTileHeight);

				final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
				if (cell == null) {
					for (int i = 0; i < player.size; i++) {
						if (coords[0][i] == col)
							if (coords[1][i] == row)
								player.get(i).draw(batch);
					}
					continue;
				}
				final TiledMapTile tile = cell.getTile();

				if (tile != null) {
					TextureRegion region = tile.getTextureRegion();

					float x1 = x + tile.getOffsetX() * unitScale;
					float y1 = y + tile.getOffsetY() * unitScale;
					float x2 = x1 + region.getRegionWidth() * unitScale;
					float y2 = y1 + region.getRegionHeight() * unitScale;

					float u1 = region.getU();
					float v1 = region.getV2();
					float u2 = region.getU2();
					float v2 = region.getV();

					vertices[X1] = x1;
					vertices[Y1] = y1;
					vertices[C1] = color;
					vertices[U1] = u1;
					vertices[V1] = v1;

					vertices[X2] = x1;
					vertices[Y2] = y2;
					vertices[C2] = color;
					vertices[U2] = u1;
					vertices[V2] = v2;

					vertices[X3] = x2;
					vertices[Y3] = y2;
					vertices[C3] = color;
					vertices[U3] = u2;
					vertices[V3] = v2;

					vertices[X4] = x2;
					vertices[Y4] = y1;
					vertices[C4] = color;
					vertices[U4] = u2;
					vertices[V4] = v1;
					
					batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
				}
				for (int i = 0; i < player.size; i++) {
					if (coords[0][i] == col)
						if (coords[1][i] == row)
							player.get(i).draw(batch);
				}
			}
		}
	}
}
