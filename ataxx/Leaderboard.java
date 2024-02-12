package ataxx;

import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabClientModels;
import com.playfab.PlayFabErrors;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class Leaderboard extends Board {
    //private final Map<String, Integer> playerToScoreMapping;
    //private final Map<Integer, Set<String>> scoreToPlayerMapping;

    public Leaderboard() {
        //this.playerToScoreMapping = new LinkedHashMap<>(5);

        //this.scoreToPlayerMapping = new TreeMap<>(Collections.reverseOrder());
    }

    public void announce () {
        System.out.println("");
    }

    private <RT> void VerifyResult(PlayFabErrors.PlayFabResult<RT> result, boolean expectSuccess)
    {
        System.out.println("Playfab login completed. Result:"+expectSuccess);
        assertNotNull(result);
        String errorMessage = CompileErrorsFromResult(result);
        if (expectSuccess)
        {
            assertNull(errorMessage, result.Error);
            assertNotNull(errorMessage, result.Result);
        }
        else
        {
            assertNull(errorMessage, result.Result);
            assertNotNull(errorMessage, result.Error);
        }
    }

    private <RT> String CompileErrorsFromResult(PlayFabErrors.PlayFabResult<RT> result) {
        if (result == null || result.Error == null)
            return null;

        String errorMessage = "";
        if (result.Error.errorMessage != null)
            errorMessage += result.Error.errorMessage;
        if (result.Error.errorDetails != null)
            for (Map.Entry<String, List<String>> pair : result.Error.errorDetails.entrySet() )
                for (String msg : pair.getValue())
                    errorMessage += "\n" + pair.getKey() + ": " + msg;
        return errorMessage;
    }

    public void addScore(String playerId, int score) {

        PlayFabClientModels.UpdatePlayerStatisticsRequest updateRequest = new PlayFabClientModels.UpdatePlayerStatisticsRequest();
        updateRequest.Statistics = new ArrayList<PlayFabClientModels.StatisticUpdate>();
        PlayFabClientModels.StatisticUpdate statUpdate = new PlayFabClientModels.StatisticUpdate();
        statUpdate.StatisticName = "AtaxxHighScores";
        statUpdate.Value = score;
        updateRequest.Statistics.add(statUpdate);
        PlayFabErrors.PlayFabResult<PlayFabClientModels.UpdatePlayerStatisticsResult> updateStatsResult = PlayFabClientAPI.UpdatePlayerStatistics(updateRequest);
        VerifyResult(updateStatsResult, true);
        System.out.println("Player statistic updated.");
/*
        PlayFabClientModels.GetLeaderboardRequest clientRequest = new PlayFabClientModels.GetLeaderboardRequest();
        clientRequest.MaxResultsCount = 50;
        clientRequest.StatisticName = "AtaxxHighScores";
        PlayFabErrors.PlayFabResult<PlayFabClientModels.GetLeaderboardResult> clientResult = PlayFabClientAPI.GetLeaderboard(clientRequest);
        VerifyResult(clientResult, true);
        assertTrue(GetClLbCount(clientResult.Result.Leaderboard) > 0);
        */

        /*
        if (playerToScoreMapping.isEmpty()) {
            playerToScoreMapping.put(playerId, score);
        }
        if (playerToScoreMapping.containsKey(playerId)) {
            int previousScore = playerToScoreMapping.get(playerId);
            if (previousScore < playerToScoreMapping.get(playerId)) {
                playerToScoreMapping.put(playerId, score);
                //scoreToPlayerMapping.get(previousScore).remove(playerId);
            }
        }
        for (Map.Entry<String, Integer> mapElement :
                playerToScoreMapping.entrySet()) {
            String key = mapElement.getKey();
            Integer value = mapElement.getValue();
            if (key == null) {
                playerToScoreMapping.put(playerId, score);
                break;
            } else if (score < value) {
                playerToScoreMapping.put(key, score);
                break;
            }
        }

         */

    }


}
