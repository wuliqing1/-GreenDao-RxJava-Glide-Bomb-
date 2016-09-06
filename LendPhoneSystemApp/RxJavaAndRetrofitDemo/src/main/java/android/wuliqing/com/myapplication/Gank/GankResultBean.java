package android.wuliqing.com.myapplication.Gank;

import java.util.List;

/**
 * Created by 10172915 on 2016/7/29.
 */
public class GankResultBean {
    /*
      "error": false,
  "results": [
    {
      "_id": "579ab0a8421aa90d36e960b4",
      "createdAt": "2016-07-29T09:26:00.838Z",
      "desc": "7.29",
      "publishedAt": "2016-07-29T09:37:39.219Z",
      "source": "chrome",
      "type": "\u798f\u5229",
      "url": "http://ww3.sinaimg.cn/large/610dc034jw1f6aipo68yvj20qo0qoaee.jpg",
      "used": true,
      "who": "\u4ee3\u7801\u5bb6"
    * */
    private boolean error;
    private List<ResultBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultBean> getResults() {
        return results;
    }

    public void setResults(List<ResultBean> results) {
        this.results = results;
    }
}
