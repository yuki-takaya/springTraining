$(function(){
	var formats = 'yyyymmdd';
	var lang = 'ja';
	$('#date').datepicker({
        format: formats,
        language: lang
    });
	$('#strDate').datepicker({
        format: formats,
        language: lang
    });
	$('#endDate').datepicker({
        format: formats,
        language: lang
    });
});
function entryCheck(form){
	var btnFlg = form.btnFlg.value;
	if(btnFlg == "insert" || btnFlg == "update"){
		var dateval = form.date.value;
		// 日付チェック
		if(dateval == ""){
			bootbox.alert("日付を入力してください。");
			return false;
		}
		// 日付妥当性チェック
		var y = dateval.substring(0,4);
		var m = dateval.substring(4,6);
		var d = dateval.substring(6,8);
		var date = new Date(y,m,d);
		if(date.getFullYear() != y || date.getMonth() != m || date.getDate() != d){
			bootbox.alert("日付が不正です。");
			return false;
		}
		// 金額数値チェック
		var amountval = form.amount.value;
		var regexp = new RegExp(/^[-]?([1-9]\d*|0)$/);
		if(!regexp.test(amountval)){
			bootbox.alert("金額には数値を入力してください。");
			return false;
		}
		// 金額バイト数チェック
		amountval = (amountval==null)?"":amountval;
		if(encodeURI(amountval).replace(/%../g, "*").length > 10){
			bootbox.alert("金額が最大値10バイトを超えています。");
			return false;
		}
		// 備考長さチェック
		var remarkval = form.remark.value;
		if(!checkLength(remarkval, 200)){
			return false;
		}
	}
	return true;
}
function checkLength(data, len){
	var result = true;
	var checkLen = data.length; 
	if(checkLen > len){
		bootbox.alert(len + "文字を超えての入力はできません。");
		result = false;
	}
	return result;
}
