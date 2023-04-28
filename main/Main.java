package main;

import java.util.Scanner;
import java.util.StringTokenizer;


public class Main {
	private static PlaySide sideToMove;
	private static PlaySide engineSide;

	public static PlaySide getEngineSide() {
		return engineSide;
	}

	private static void toggleSideToMove() {
		sideToMove = switch (sideToMove) {
			case BLACK -> PlaySide.WHITE;
			case WHITE -> PlaySide.BLACK;
			default -> PlaySide.NONE;
		};
	}

	private static String constructFeaturesPayload() {
		return "feature"
				+ " done=0"
				+ " sigint=0"
				+ " sigterm=0"
				+ " san=0"
				+ " reuse=0"
				+ " usermove=1"
				+ " analyze=0"
				+ " ping=0"
				+ " setboard=0"
				+ " level=0"
				+ " variants=\"crazyhouse\""
				+ " name=\"" + Bot.getBotName()
				+ "\" myname=\"" + Bot.getBotName()
				+ "\" done=1";
	}

	private static class EngineComponents {
		private enum EngineState {
			HANDSHAKE_DONE, RECV_NEW, PLAYING, FORCE_MODE
		}

		public Bot bot;
		private EngineState state;
		private final Scanner scanner;
		private String bufferedCmd;
		private boolean isStarted;

		public EngineComponents() {
			this.bot = new Bot();
			this.state = null;
			this.scanner = new Scanner(System.in);
			this.bufferedCmd = null;
			this.isStarted = false;
		}

		private void newGame() {
			bot = new Bot();
			state = EngineState.RECV_NEW;
			System.out.println("Setting engine side to NONE");
			engineSide = PlaySide.NONE;
			sideToMove = PlaySide.WHITE;
			this.isStarted = false;
		}

		private void enterForceMode() {
			state = EngineState.FORCE_MODE;
		}

		private void leaveForceMode() {
			/* Called upon receiving "go" */
			state = EngineState.PLAYING;

			System.out.println("Calculate move because of leave force mode isStart: " + isStarted);
			if (!isStarted) {
				isStarted = true;
				System.out.println("Setting engine side to sideToMove: " + sideToMove);
				engineSide = sideToMove;
			}

			/* Make next move (go is issued when it's the bot's turn) */
			System.out.println("Calculate move because of leave force mode isStart: " + isStarted + " sideToMove: " + getEngineSide());
			Move nextMove = bot.calculateNextMove(engineSide);
			emitMove(nextMove);

			toggleSideToMove();
		}

		private void processIncomingMove(Move move) {
			switch (state) {
				case RECV_NEW -> {
					state = EngineState.PLAYING;

					bot.recordMove(move, sideToMove);
					toggleSideToMove();
					engineSide = sideToMove;

					if (sideToMove == PlaySide.WHITE) {
						Move response = bot.calculateNextMove(engineSide);
						emitMove(response);
						toggleSideToMove();
					}
				}
				case FORCE_MODE -> {
					/* Record move for side to move in internal structures */
					bot.recordMove(move, sideToMove);
					toggleSideToMove();
				}
				case PLAYING -> {
					bot.recordMove(move, sideToMove);
					toggleSideToMove();

					Move response = bot.calculateNextMove(engineSide);
					emitMove(response);
					toggleSideToMove();
				}
				default -> System.err.println("[WARNING]: Unexpected move received (prior to new command)");
			}
		}

		private void emitMove(Move move) {
			if (move.isDropIn() || move.isNormal() || move.isPromotion())
				System.out.print("move ");
			System.out.println(Move.serializeMove(move));
		}

		public void performHandshake() {
			/* Await start command ("xboard") */
			String command = scanner.nextLine();
			if (!command.equals("xboard")) {
				System.err.println("PROTOCOL ERROR: EXPECTED XBOARD");
				System.exit(-1);
			}
			System.out.print("\n");

			command = scanner.nextLine();
			if (!command.equals("protover 2")) {
				System.err.println("PROTOCOL ERROR: PROTOVER != 2 OR UNEXPECTED CMD");
				System.exit(-1);
			}

			/* Respond with features */
			String features = constructFeaturesPayload();
			System.out.println(features);

			/* Receive and discard boilerplate commands */
			while (true) {
				command = scanner.nextLine();
				if (command.equals("new") || command.equals("force")
						|| command.equals("go") || command.equals("quit")) {
					bufferedCmd = command;
					System.out.println("Break because of " + command);
					break;
				}
			}

			state = EngineState.HANDSHAKE_DONE;
		}

		public void executeOneCommand() {
			String nextCmd;

			if (bufferedCmd != null) {
				nextCmd = bufferedCmd;
				bufferedCmd = null;
			} else {
				nextCmd = scanner.nextLine();
			}

			StringTokenizer tokenizer = new StringTokenizer(nextCmd);
			String command = tokenizer.nextToken();

			switch (command) {
				case "quit" -> System.exit(0);
				case "new" -> newGame();
				case "force" -> enterForceMode();
				case "go" -> leaveForceMode();
				case "usermove" -> {
					Move incomingMove = Move.deserializeMove(tokenizer.nextToken());
					processIncomingMove(incomingMove);
				}
				// Debug commands
				case "hint" -> System.out.println("askuser command Insert debug command");
				case "command" -> parseCommands(tokenizer.nextToken());
			}
		}

		private void parseCommands(String command) {
			switch (command) {
				case "boardw" -> DebugTools.printBoardPretty(bot.board.getBoard(), true);
				case "boardb" -> DebugTools.printBoardPretty(bot.board.getBoard(), false);
			}
			if (command.startsWith("eval")) {

			}

		}
	}

	public static void main(String[] args) {
		EngineComponents engine = new EngineComponents();
		engine.performHandshake();

		while (true) {
			/* Fetch and execute next command */
			engine.executeOneCommand();
		}
	}
}
