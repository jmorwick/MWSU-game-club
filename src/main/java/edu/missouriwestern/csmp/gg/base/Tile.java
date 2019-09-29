package edu.missouriwestern.csmp.gg.base;

import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/** Represents spaces on the {@link Board} that contain entities */
public class Tile implements Container, HasProperties {
	public final int row;
	public final int column;
	private final Board board;
	private final String type;
	private final Map<String,String> properties;

	/**
	 * Constructs a tile from a given {@link Board} at a given location with the given character representation
	 * @param board given Board
	 * */
	protected Tile(Board board, int column, int row, String type, Map<String,String> properties) {
		this.board = board;
		this.row = row;
		this.column = column;
		this.type = type;
		this.properties = new ConcurrentHashMap<>(properties);
	}

	/**
	 * Constructs a tile from a given {@link Board} at a given location with the given character representation
	 * @param board given Board
	 * */
	protected Tile(Board board, int column, int row, String type) {
		this(board, column, row, type, new HashMap<>());
	}


	/** row placement of the tile on the board */
	public int getRow() {
		return row;
	}

	/** column placement of the tile on the board */
	public int getColumn() {
		return column;
	}

	@Override
	public Game getGame() { return board.getGame(); }

	public String getType() { return type; }

	/**
	 * Return the {@link Board} associated with this tile
	 * @return associated Board
	 */
	public Board getBoard() { return board;}

	@Override
	public Map<String,String> getProperties() {
		return properties;
	}

	@Override
	public void setProperty(String key, String value) {
		properties.put(key, value);
		board.getGame().propagateEvent(tileStatusUpdateEvent());
	}

	/** returns a JSON representation of this tile and its properties
	 */
	@Override
	public String toString() {
		var gsonBuilder = new GsonBuilder();
		var gson = gsonBuilder.create();
		var m = new HashMap<String,Object>();
		m.put("row", getRow());
		m.put("column", getColumn());
		m.put("board", getBoard().getName());
		m.put("type", type);
		m.put("properties", properties);
		return gson.toJson(m);
	}

	public Event tileStatusUpdateEvent() {
		return new Event(getGame(), "tile-status-update",
				Map.of(
						"board", getBoard().getName(),
						"row", row+"",
						"column", column+""
				));
	}

}
