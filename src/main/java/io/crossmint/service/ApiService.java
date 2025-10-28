package io.crossmint.service;

import io.crossmint.models.Cometh;
import io.crossmint.models.Soloon;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface ApiService {

  ResponseEntity<?> createPolyanet(HttpEntity<String> entity);

  void deletePolyanet(int row, int column);

  Soloon createSoloon(int row, int column, String color);

  void deleteSoloon(int row, int column);

  Cometh createCometh(int row, int column, String direction);

  void deleteCometh(int row, int column);
}
