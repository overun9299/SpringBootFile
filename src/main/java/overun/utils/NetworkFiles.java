package overun.utils;

/**
 * @ClassName: NetworkFiles
 * @Description: 此方法适用于下载网络文件并且实现可编辑改名，不需要有后台代码前端JS可实现。
 * @author: 薏米滴答-西安-ZhangPY
 * @version: V1.0
 * @date: 2019/6/10 11:51
 * @Copyright: 2019 www.yimidida.com Inc. All rights reserved.
 */
public class NetworkFiles {

    /**
     * 下载
     * @param  {String} url 目标文件地址
     * @param  {String} filename 想要保存的文件名称
     *
     *     downloadRecord(url, filename) {
     *         this.getBlob(url).then(blob => {
     *                 this.saveAs(blob, filename);
     *             });
     *     }
     *
     */


    /**
     * 获取 blob
     * @param  {String} url 目标文件地址
     * @return {Promise}
     *
     *     getBlob(url) {
     *         return new Promise(resolve => {
     *                 const xhr = new XMLHttpRequest();
     *
     *         xhr.open("GET", url, true);
     *         xhr.responseType = "blob";
     *         xhr.onload = () => {
     *             if (xhr.status === 200) {
     *                 resolve(xhr.response);
     *             }
     *         };
     *
     *         xhr.send();
     *             });
     *     }
     */


    /**
     * 保存
     * @param  {Blob} blob
     * @param  {String} filename 想要保存的文件名称
     *
     *     saveAs(blob, filename) {
     *         if (window.navigator.msSaveOrOpenBlob) {
     *             navigator.msSaveBlob(blob, filename);
     *         } else {
     *                 const link = document.createElement("a"),
     *                     body = document.querySelector("body");
     *
     *             link.href = window.URL.createObjectURL(blob);
     *             link.download = filename;
     *
     *             // fix Firefox
     *             link.style.display = "none";
     *             body.appendChild(link);
     *
     *             link.click();
     *             body.removeChild(link);
     *
     *             window.URL.revokeObjectURL(link.href);
     *         }
     *     }
     */




}
