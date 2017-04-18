package models.dao

import com.google.inject.{Inject, Singleton}
import models.tables.SlickTables
import org.slf4j.LoggerFactory
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
/**
  * User: liboren.
  * Date: 2017/4/14.
  * Time: 16:27.
  */
@Singleton
class KeywordResponseDao @Inject()(
                          protected val dbConfigProvider: DatabaseConfigProvider
                        ) extends HasDatabaseConfigProvider[JdbcProfile] {

  val log = LoggerFactory.getLogger(this.getClass)
  import slick.driver.MySQLDriver.api._
  private [this] val tKeywordresponse = SlickTables.tKeywordresponse

  /**
  * 增加新的关键词回复
  * @param keyword 关键词
  * @param restype 回复类型，1-文本 ，2-图片
  * @param response 文本内容或图片地址
  * @param triggertype 触发类型，1-精确匹配，0-模糊匹配
  * @param userid 用户id
  * @param groupid 群组id
  * @return 新增的自增id
  * */
  def createrKeywordResponse(keyword:String,restype:Int,response:String,triggertype:Int,userid:Long,groupid:Long) ={
    db.run(tKeywordresponse.map(i => (i.keyword,i.restype,i.response,i.triggertype,i.userid,i.groupid)).returning(tKeywordresponse.map(_.id)) +=
      (keyword,restype,response,triggertype,userid,groupid)
    ).mapTo[Long]
  }

  /**
    * 获取该用户的关键词回复列表
    * @param userid 用户id
    * @return 该用户的关键词回复列表
    * */
  def getKeywordResponseList(userid:Long) = db.run(
    tKeywordresponse.filter(m => m.userid === userid).result
  )

  /**
    * 修改关键词回复信息
    * @param id 自增id
    * @param keyword 关键词
    * @param restype 回复类型，1-文本 ，2-图片
    * @param response 文本内容或图片地址
    * @param triggertype 触发类型，1-精确匹配，0-模糊匹配
    * @param userid 用户id
    * @param groupid 群组id
    * @return 更新结果
    * */
  def changeKeywordResponseList(id:Long,userid:Long,keyword:String,restype:Int,response:String,triggertype:Int,groupid:Long) = {
    db.run(tKeywordresponse.filter( m => m.userid === userid && m.id === id).map(m => (m.keyword,m.restype,m.response,m.triggertype))
      .update(keyword,restype,response,triggertype))
  }

  /**
    * 删除关键词回复信息
    * @param id 自增id
    * @param userid 用户id
    * @return 删除结果
    * */
  def deleteKeywordResponse(id:Long,userid:Long) = db.run(
    tKeywordresponse.filter( m => m.id === id && m.userid === userid).delete
  )

}

