/**
 * Created by falcon on 17-1-5.
 * <pre>
 * 1.search user(account).
 *      month_hours<=now    //pass some time.
 *      month_hours!=0      //not default.
 *      vlan_id=1           //need push.
 *      subslot_s=0         //white list.
 * 2.filter white list.
 *      subslot_s=0 or subslot_s is null
 *          -> need push.
 *      else not push
 *          -> update user set vlan_id=10.
 * 3.search crm.
 *      return sign=0   //need push
 *      if not need
 *          -> update user set vlan_id=5
 *          -> insert into cp_push_sign_mod_t_t
 * 4.search online info.
 *      search session by login.
 *      search bras by session.
 *      convert user(account), session, bras to CoaInfo.
 *      maybe user offline.
 * 5.send coa to bras.(kick off)
 *      send coa.
 *      success
 *          -> update user set vlan_id=2
 *          -> insert into cp_push_sign_mod_t_t
 *      fail
 *          -> update user set vlan_id=7
 *          -> insert into cp_push_sign_mod_t_t
 * 6.sleep 10 minutes.
 * </pre>
 */
package com.ge.scanner;