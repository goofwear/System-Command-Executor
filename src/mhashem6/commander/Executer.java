/**
 * Commander Library is a simple Library made to make the process of executing
 * command lines through java programs simpler. Copyright (C) 2016 mhashem6 >
 * (Muhammad Hashim)
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * this library is the base of Simple-ADB program :
 * http://forum.xda-developers.com/android/software/revive-simple-adb-tool-t3417155
 * you can contact me @ abohashem.com@gmail.com Source :
 *
 */

package mhashem6.commander;

import java.io.IOException;

import mhashem6.commander.exceptions.*;

/**
 * The class the can execute/abort a command, with ability of producing and
 * writing output.
 * 
 * @see CommandI
 * @author mhashim6 (Muhammad Hashim)
 */
public class Executer {

	protected ProcessBuilder pb;
	protected Process	 process;

	protected ExecuterStreamFormatter eStreamFormatter;

	protected int exitval = 777;

	/**
	 * 
	 * A constructor that requires a pre-initialized ExecuterStreamFormatter
	 * object to handle the output.
	 * 
	 * @param eStreamFormatter
	 * 
	 * @see ExecuterStreamFormatter
	 */
	public Executer(ExecuterStreamFormatter eStreamFormatter) {
		this.eStreamFormatter = eStreamFormatter;
	}
	// ============================================================

	/**
	 * a method that executes the command line generated from Command
	 * object.
	 * 
	 * @param command
	 * @throws IOException
	 */
	public synchronized void executeCommand(CommandI command) throws UnrecognisedCmdException {
		exitval = 777;
		execute(command);

		recordOutput();

		abort();
	}
	// ============================================================

	/**
	 * a method that executes the command line generated from Command
	 * object, doesn't produce output.
	 * 
	 * @param command
	 * @throws UnrecognisedCmdException
	 */
	protected void execute(CommandI command) throws UnrecognisedCmdException {

		try {
			pb = new ProcessBuilder(command.executableCommand());
			process = pb.start();
		}
		catch (IOException e) {
			throw new UnrecognisedCmdException(command.toString());
		}
	}
	// ============================================================

	/**
	 * the method responsible for passing the output to the
	 * ExecuterStreamReader object.
	 * 
	 * @throws IOException
	 * 
	 */
	protected void recordOutput() {
		eStreamFormatter.getAppender().appendText("--powered by commander library."
				+ System.getProperty("line.separator") + System.getProperty("line.separator")); // DO NOT MODIFY.
		eStreamFormatter.FormatStdStream(process.getInputStream());
		eStreamFormatter.FormatErrStream(process.getErrorStream());
	}
	// ============================================================

	/**
	 * a method that aborts the current command process
	 * 
	 * @throws IOException
	 * 
	 * @see Executer#isAlive()
	 */
	public void abort() {
		if (isAlive()) {
			process.destroyForcibly();
			exitval = process.exitValue();
		}
		eStreamFormatter.closeResources();
	}
	// ============================================================

	/**
	 * 
	 * @return true if the command execution is still running
	 */
	public boolean isAlive() {
		return (process == null) ? false : process.isAlive();
	}
	// ============================================================

	/**
	 * 
	 * @return exit value of the process, or 777 if the exit value couldn't
	 *         be retrieved successfully.
	 */
	public int getExitValue() {
		return exitval;
	}

}
