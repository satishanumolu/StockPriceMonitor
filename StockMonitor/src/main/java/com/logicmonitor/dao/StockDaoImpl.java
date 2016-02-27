package com.logicmonitor.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.logicmonitor.entity.StockData;

@Repository
public class StockDaoImpl implements StockDao{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<StockData> findAllStocks() {
		TypedQuery<StockData> query = em.createQuery("SELECT s FROM StockData s ORDER BY s.symbol ASC", StockData.class);
    	List<StockData> stocks = query.getResultList();
    	return stocks;
	}

	@Override
	public StockData findStockBySymbol(String symbol) {
		return em.find(StockData.class, symbol);
	}

	@Override
	@Transactional
	public void addStock(StockData stock) {
		em.persist(stock);
	}

	@Override
	@Transactional
	public void deleteStock(String symbol) {
		Query query = em.createQuery(
			      "DELETE FROM StockData s WHERE s.symbol = :pSymbol");
		query.setParameter("pSymbol", symbol).executeUpdate();
	}

}
