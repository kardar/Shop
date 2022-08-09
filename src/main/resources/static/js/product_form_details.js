// $(document).ready(function () {
//     $("a[name='linkRemoveDetails']").each(function (index) {
//         $(this).click(function () {
//             alert("each click")
//             removeDetailSectionByIndex(index);
//         })
//     })
//
// })



 function addNextDetailSection(){
    allDivDetails = $("[id^='divDetail']");
    divDetailCount = allDivDetails.length;

     htmlDetailSection = `
      <div class="form-inline" id="divDetail${divDetailCount}">
      <input type="hidden" name="detailIDs" th:value="0">
       <label class="m-3">Name :</label>
       <input type="text" class="form-control w-25" name="detailNames" maxlength="255">

       <label class="m-3">Value :</label>
       <input type="text" class="form-control w-25" name="detailValues"
            maxlength="255">
   </div>
     `;
     previousDivDetailsSection = allDivDetails.last();
     previousDivDetailsID = previousDivDetailsSection.attr("id");
     $("#divProductDetails").append(htmlDetailSection);

     htmlLinkRemove = `<a class="btn fas fa-times-circle fa-2x icon-dark "
          href="javascript:removeDetailSectionById('${previousDivDetailsID}')"
               title="Remove this Detail"></a>`;
     previousDivDetailsSection.append(htmlLinkRemove);
 }
 function removeDetailSectionById(id){
    $("#"+id).remove();
 }