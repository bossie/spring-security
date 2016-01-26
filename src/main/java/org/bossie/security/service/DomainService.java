package org.bossie.security.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DomainService {
	public Map<String, Object> getItem(long itemId) {
		// TODO: replace with Mongo Document

		@SuppressWarnings("serial")
		Map<String, Object> item = new HashMap<String, Object>() {{
			put("_id", itemId);
			put("collection_id", itemId);
		}};

		return item;
	}
}
