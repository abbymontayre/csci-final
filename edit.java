private void setupEntities() {
        // Set up players
        Player player1 = new Player(1, 100, 100, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE, map);
        Player player2 = new Player(2, 831, 447, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE, map);
        
        if (playerID == 1) {
            currentPlayer = player1;
            otherPlayer = player2;
            player1.setKeyHandler(keyHandler);
        } else {
            currentPlayer = player2;
            otherPlayer = player1;
            player2.setKeyHandler(keyHandler);
        }

        // Set up guide item
        GuideItem guideItem = new GuideItem(258, 469, "A Poet's Keepsake", 
            "Begin at dawn when one stands tall, Skip the noon, let the evening call. Catch the twilight in between, The ticking riddle sits unseen.",       
            Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT);
        guideItem.setPlayerAndHandler(currentPlayer, keyHandler);
        guideItems.add(guideItem);
        entities.add(guideItem);

        // Set up plates
        int[][] platePositions = {{200, 200}, {400, 200}, {600, 200}};
        for (int i = 0; i < 3; i++) {
            Plate plate = new Plate(i + 1, 
                platePositions[i][0], 
                platePositions[i][1], 
                Constants.GAME_SETTINGS.TILE_SIZE, 
                Constants.GAME_SETTINGS.TILE_SIZE);
            plate.setPlayerAndHandler(currentPlayer, keyHandler);
            plates.add(plate);
            entities.add(plate);
        }

        // Set up portals
        portal1 = new Portal(1, 768, 69, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
        portal2 = new Portal(2, 123, 630, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
        
        // Set portal-player relationships
        if (playerID == 1) {
            portal1.setPlayer(currentPlayer);
            portal2.setPlayer(otherPlayer);
        } else {
            portal2.setPlayer(currentPlayer);
            portal1.setPlayer(otherPlayer);
        }

        entities.add(portal1);
        entities.add(portal2);
        entities.add(player1);
        entities.add(player2);
    }

    