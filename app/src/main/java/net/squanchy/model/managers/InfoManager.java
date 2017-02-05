package net.squanchy.model.managers;

import android.content.Context;

import net.squanchy.model.PreferencesManager;
import net.squanchy.model.dao.InfoDao;
import net.squanchy.model.data.InfoItem;
import net.squanchy.service.api.SquanchyRepository;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

public class InfoManager extends SynchronousItemManager<InfoItem.General, String> {

    private InfoDao infoDAO;
    private final PreferencesManager preferencesManager;

    public InfoManager(Context context) {
        super(context);
        infoDAO = new InfoDao(context);
        preferencesManager = PreferencesManager.create(context);
    }

    @Override
    protected String getEntityRequestTag() {
        return "info";
    }

    @Override
    protected boolean storeResponse(InfoItem.General requestResponse, String tag) {
        List<InfoItem> infoList = requestResponse.getInfo();
        if (infoList == null) {
            return false;
        }

        infoDAO.saveOrUpdateDataSafe(infoList);
        for (InfoItem info : infoList) {
            if (info != null) {
                if (info.isDeleted()) {
                    infoDAO.deleteDataSafe(info.getId());
                }
            }
        }
        preferencesManager.saveMajorInfoTitle(requestResponse.getMajorTitle());
        preferencesManager.saveMinorInfoTitle(requestResponse.getMinorTitle());

        return true;
    }

    @Override
    protected Observable<InfoItem.General> doFetch(SquanchyRepository repository) {
        return repository.info();
    }

    public List<InfoItem> getInfo() {
        List<InfoItem> infoItems = infoDAO.getAllSafe();
        Collections.sort(infoItems, (infoItem, infoItem2) -> Double.compare(infoItem.getOrder(), infoItem2.getOrder()));

        return infoItems;
    }

    public void clear() {
        infoDAO.deleteAll();
    }
}
