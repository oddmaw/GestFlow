package com.example.services;

import com.example.dao.CommandDAO;
import com.example.models.Command;

import java.sql.SQLException;
import java.util.List;

public class CommandService {
    private CommandDAO commandDAO;

    public CommandService(CommandDAO commandDAO) {
        this.commandDAO = commandDAO;
    }
    public Command getCommandById(int id) throws SQLException {
        return commandDAO.getCommandById(id);
    }
    public List<Command> getAllCommands() throws SQLException {
        return commandDAO.getAllCommands();
    }
    public int addCommand(Command command) throws SQLException {
        return commandDAO.addCommand(command);
    }
    public int updateCommand(Command command) throws SQLException {
        return commandDAO.updateCommand(command);
    }
    public int deleteCommand(int id) throws SQLException {
        return commandDAO.deleteCommand(id);
    }
}
