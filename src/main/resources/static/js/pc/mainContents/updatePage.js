<script type="text/javascript">
	const boardId = $("#boardId").val();

	$(document).on('click', '#btn_edit', function(e) {
		if (confirm("정말 수정하시겠습니까 ?") == true) {

			//데이터를 담아내는 부분
			const boardTitle = $("#boardTitle").val().trim();
			const boardContent = $("#boardContent").val().trim();
			const uploadFile = $("#uploadFile")[0].files[0];

			if(boardTitle === ''){
				alert('제목을 입력해주세요.');
				return;
			}

			if(boardContent === ''){
				alert('내용을 입력해주세요.');
				return;
			}

			//ajax 통신을 사용해 서버에 데이터를 전송하기 위해
			//폼데이터 객체를 생성함
			//append를 통해서 프로퍼티에 바인딩이 가능하도록 세팅한다.
			var formData = new FormData();
			formData.append("boardId",boardId);
			formData.append("boardTitle",boardTitle);
			formData.append("boardContent",boardContent);

			//만약 uploadFile이 undifined거나 null일 경우 폼데이터에 보내지 않도록 한다.
			//이부분 체크하지 않을 경우 undifined가 데이터로 보내지기 때문에 서버에서 에러가 발생한다.
			if(uploadFile)
				formData.append("uploadFile",uploadFile);

			//ajax로 파일전송 폼데이터를 보내기위해
			//enctype, processData, contentType 이 세가지를 반드시 세팅해야한다.
			$.ajax({
				enctype: 'multipart/form-data',
				processData: false,
				contentType: false,
				cache: false,
				url : "./updateTest.do",
				data : formData,
				type : "POST",
				success : function(res){
					alert('수정 완료');
					location.href='./testList.do';
				}
			})
		}
	});