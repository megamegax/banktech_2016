package com.banktech.javachallenge.service.domain.logic;

import com.banktech.javachallenge.view.domain.ViewModel;

import java.io.IOException;


public interface GameLogic {
    
    ViewModel step(ViewModel currentViewModel) throws IOException;

}