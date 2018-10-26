package com.company;

import java.rmi.*;

public interface DropMusic_C_I extends Remote{
	public void Print(String s) throws RemoteException;
	public void ChangeUserToEditor(boolean change) throws RemoteException;
	public void ping() throws RemoteException;
}