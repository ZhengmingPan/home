package com.home.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.common.service.JpaServiceImpl;
import com.home.common.utils.FileUploadUtils;
import com.home.core.entity.Annex;
import com.home.core.repository.jpa.AnnexDao;
import com.home.core.utils.FastDFSClient;
import com.home.core.vo.FastDFSFile;
import com.home.core.web.CoreThreadContext;

@Service
@Transactional(readOnly = true)
public class AnnexService extends JpaServiceImpl<Annex, String> {

	private static Logger LOGGER = LoggerFactory.getLogger(AnnexService.class);

	@Value("${upload.folder}")
	private String root;

	@Autowired
	private AnnexDao annexDao;

	@Override
	@Transactional(readOnly = false)
	public Annex save(Annex annex) {
		annex.setCreateTime(new Date());
		annex.setCreator(CoreThreadContext.getUserId());
		return super.save(annex);
	}
 

	@Transactional(readOnly = false)
	public Annex upload(byte[] data, String fileName, String objectId, String objectType, String path) {
		String fileType = FilenameUtils.getExtension(fileName).toLowerCase(Locale.ENGLISH);
		String filePath = FileUploadUtils.storeFile(data, root, path, fileType);

		Annex annex = new Annex();
		annex.setName(fileName);
		annex.setObjectId(objectId);
		annex.setObjectType(objectType);
		annex.setPath(filePath);
		annex.setType(fileType);

		try { 
			FastDFSFile file = new FastDFSFile(fileName, data, fileType);
			String[] fileAbsolutePath = FastDFSClient.upload(file);
			String fastdfsUrl = FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
			annex.setFastdfsUrl(fastdfsUrl);
		} catch (Exception e) {
			LOGGER.error("FastDFS 文件上传失败");
		} finally {
			annex = save(annex);
		}

		return annex;
	}

	@Transactional(readOnly = false)
	public void remove(String id) {
		Annex annex = super.get(id);
		File file = new File(root, annex.getPath());
		FileUtils.deleteQuietly(file);
		super.delete(id);
	}

	public Page<Annex> page(final String searchKey, Pageable page) {
		return annexDao.findAll(new Specification<Annex>() {
			@Override
			public Predicate toPredicate(Root<Annex> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();

				if (StringUtils.isNotEmpty(searchKey)) {
					Predicate p1 = cb.like(root.get(Annex.PROP_PATH).as(String.class),
							"%" + StringUtils.trimToEmpty(searchKey) + "%");
					Predicate p2 = cb.like(root.get(Annex.PROP_NAME).as(String.class),
							"%" + StringUtils.trimToEmpty(searchKey) + "%");
					predicates.add(cb.or(p1, p2));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, page);
	}

	public Annex getByPath(String path) {
		return annexDao.getByPath(path);
	}

}
